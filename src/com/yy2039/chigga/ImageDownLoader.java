package com.yy2039.chigga;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;

public class ImageDownLoader {
    private static ImageDownLoader yy_image_downloader = null;
    public static void init( Context context ) {
        yy_image_downloader = new ImageDownLoader( context );
    }
    public static ImageDownLoader getInstance() {
        return yy_image_downloader;
    }

    /** 
     * 缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存 
     */  
    private LruCache<String, Bitmap> mMemoryCache;
    /** 
     * 操作文件相关类对象的引用 
     */  
    private FileUtils fileUtils;
    /** 
     * 下载Image的线程池 
     */  
    private ExecutorService mImageThreadPool = null;

    public ImageDownLoader( Context context ) {
        //获取系统分配给每个应用程序的最大内存，每个应用系统分配32M  
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int mCacheSize = maxMemory / 8;
        //给LruCache分配1/8 4M  
        mMemoryCache = new LruCache<String, Bitmap>( mCacheSize ) {
            //必须重写此方法，来测量Bitmap的大小  
            @Override
            protected int sizeOf( String key, Bitmap value ) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        fileUtils = new FileUtils( context );
    }

    /** 
     * 获取线程池的方法，因为涉及到并发的问题，我们加上同步锁 
     * @return 
     */  
    public ExecutorService getThreadPool() {
        if( mImageThreadPool == null ) {
            synchronized( ExecutorService.class ) {
                if( mImageThreadPool == null ) {
                    //为了下载图片更加的流畅，我们用了2个线程来下载图片  
                    mImageThreadPool = Executors.newFixedThreadPool( 2 );
                }
            }
        }

        return mImageThreadPool;
    }

    /** 
     * 添加Bitmap到内存缓存 
     * @param key 
     * @param bitmap 
     */  
    public void addBitmapToMemoryCache( String key, Bitmap bitmap ) {
        if( getBitmapFromMemCache( key ) == null && bitmap != null ) {
            mMemoryCache.put( key, bitmap );
        }
    }

    /** 
     * 从内存缓存中获取一个Bitmap 
     * @param key 
     * @return 
     */  
    public Bitmap getBitmapFromMemCache( String key ) {
        return mMemoryCache.get( key );
    }

    class AsyncLoadResult extends Object {
        String url;
        Bitmap bitmap;
        AsyncLoadResult( String u, Bitmap b ) { url = u; bitmap = b; }
    }

    private Map<String, List<onImageLoaderListener>> download_listeners = new HashMap<String, List<onImageLoaderListener>>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage( Message msg ) {
            AsyncLoadResult result = (AsyncLoadResult)msg.obj;

            // 将 Bitmap 加入内存缓存  
            addBitmapToMemoryCache( result.url, result.bitmap );

            List<onImageLoaderListener> url_listeners = download_listeners.get( result.url );
            if( url_listeners != null ) {
                for( int i=0; i < url_listeners.size(); ++i ) {
                    onImageLoaderListener listener = url_listeners.get( i );
                    listener.onImageLoader( result.bitmap, result.url );
                }

                download_listeners.remove( result.url );
            }

            result.bitmap = null;
        }
    };

    /** 
     * 先从内存缓存中获取Bitmap,如果没有就从SD卡或者手机缓存中获取，SD卡或者手机缓存 
     * 没有就去下载 
     * @param url 
     * @param listener 
     * @return 
     */  
    public Bitmap downloadImage( final String url, final onImageLoaderListener listener ) {
        //替换Url中非字母和非数字的字符，这里比较重要，因为我们用Url作为文件名，比如我们的Url  
        //是Http://xiaanming/abc.jpg;用这个作为图片名称，系统会认为xiaanming为一个目录，  
        //我们没有创建此目录保存文件就会报错  
        Bitmap bitmap = getBitmapFromMemCache( url );
        if( bitmap != null ) {
            return bitmap;
        } else {
            List<onImageLoaderListener> url_listeners = download_listeners.get( url );
            if( url_listeners == null ) {
                url_listeners = new ArrayList<onImageLoaderListener>();
                url_listeners.add( listener );

                download_listeners.put( url, url_listeners );
            } else {
                url_listeners.add( listener );
            }

            getThreadPool().execute( new Runnable() {
                @Override
                public void run() {
                    final String subUrl = url.replaceAll( "[^\\w]", "" );

                    // 从 SD 卡获取手机里面获取 Bitmap 
                    if ( fileUtils.isFileExists( subUrl ) && fileUtils.getFileSize( subUrl ) != 0 ) {
                        Bitmap bitmap = fileUtils.getBitmap( subUrl );

                        Message msg = handler.obtainMessage();
                        msg.obj = new AsyncLoadResult( url, bitmap );
                        handler.sendMessage( msg );
                    } else {
                        Bitmap bitmap = getBitmapFormUrl( url );

                        Message msg = handler.obtainMessage();
                        msg.obj = new AsyncLoadResult( url, bitmap );
                        handler.sendMessage( msg );

                        try {  
                            //保存在SD卡或者手机目录  
                            fileUtils.savaBitmap( subUrl, bitmap );
                        } catch ( IOException e ) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        return null;
    }

    /** 
     * 从Url中获取Bitmap 
     * @param url 
     * @return 
     */  
    private Bitmap getBitmapFormUrl( String url ) {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        try {
            URL mImageUrl = new URL( url );
            con = (HttpURLConnection)mImageUrl.openConnection();
            con.setConnectTimeout( 10 * 1000 );
            con.setReadTimeout( 10 * 1000 );
            con.setDoInput( true );
            //con.setDoOutput( true );
            bitmap = BitmapFactory.decodeStream( con.getInputStream() );
        } catch ( UnsupportedEncodingException e ) {
            e.printStackTrace();
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            if( con != null ) {
                con.disconnect();
            }
        }

        return bitmap;
    }

    /** 
     * 取消正在下载的任务 
     */  
    public synchronized void cancelTask() {
        if( mImageThreadPool != null ) {
            mImageThreadPool.shutdownNow();
            mImageThreadPool = null;
        }
    }

    /** 
     * 异步下载图片的回调接口 
     * @author len 
     * 
     */  
    public interface onImageLoaderListener {
        void onImageLoader( Bitmap bitmap, String url );
    }
}
