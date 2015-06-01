/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.arjinmc.phonegapdemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.cordova.Config;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.IceCreamCordovaWebViewClient;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.arjinmc.phonegapdemo.R;

public class TestDemo extends Activity implements CordovaInterface{
	
	private CordovaWebView webView;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //this is the default sample to load the config url from local
        //super.loadUrl(Config.getStartUrl());
        
        //now we use customer layout to get the request statues
        setContentView(R.layout.main);
        
        webView = (CordovaWebView) findViewById(R.id.tutorialView);
        
        //init cordovaInterface
        CordovaInterface cordovaInterface = (CordovaInterface) this;
        CordovaWebViewClient cordovaWebViewClient = new CordovaWebViewClient(cordovaInterface, webView) {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            	//we use CordovaWebView postMessage() to CordovaInterface 
            	//like handle to get what CordovaWebView has sent
            	//post format like message_id,content
                webView.postMessage("onPageStarted ", url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            	webView.postMessage("onPageFinished ", url);
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	webView.postMessage("onReceivedError ", failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        };
        
        //remember to init config if you don't use the default Config.getStartUrl()
        //or the url cannot be loaded
        Config.init(this);
        
        webView.loadUrl("http://arjinmc.com");
    }

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public ExecutorService getThreadPool() {
		return threadPool;
	}

	@Override
	public Object onMessage(String id, Object data) {
		//here is for get the data what CordovaWebView has sent 
		//rememeber to check if data is not null
		//becuz when CordovaWebView has been destoryed it still post a null message like about:blank
		if(data!=null){
			Log.e("post message",data.toString()+" "+id);
		}
		
		return data;
	}

	@Override
	public void setActivityResultCallback(CordovaPlugin arg0) {
		
	}

	@Override
	public void startActivityForResult(CordovaPlugin arg0, Intent arg1, int arg2) {
		
	}
	
	@Override
	protected void onDestroy() {
		//remember to cleanup all PhoneGap related stuff
		//handleDestroy() will cleanup stuff like the PluginManager, broadcast receivers
		if(webView!=null){
			 webView.handleDestroy();
		}
		super.onDestroy();
	}
    
}

