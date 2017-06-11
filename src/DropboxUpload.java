import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

public class DropboxUpload {
	
	DropboxUpload(){
		
	}
	
	public void uploadFile() throws DbxException, IOException{
		DbxRequestConfig config = new DbxRequestConfig(
				"JavaTutorial/1.0", Locale.getDefault().toString());

		String accessToken = "5MGtErqa5jMAAAAAAAAAHKBaMeTDAkyr11E-pmt2gtMC0shCrUlVwa8vwU0ZGliZ";

		DbxClient client = new DbxClient(config, accessToken);

		//client.delete("/points.txt");
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		File inputFile = new File("points.txt");
		FileInputStream inputStream = new FileInputStream(inputFile);
		try {
			client.uploadFile("/points-" + timeStamp +".txt", DbxWriteMode.add(), inputFile.length(), inputStream);
		} finally {
			inputStream.close();
		}
	}

}
