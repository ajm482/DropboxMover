import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.temboo.Library.Dropbox.FileOperations.DeleteFileOrFolder;
import com.temboo.Library.Dropbox.FileOperations.DeleteFileOrFolder.DeleteFileOrFolderInputSet;
import com.temboo.Library.Dropbox.FileOperations.DeleteFileOrFolder.DeleteFileOrFolderResultSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.GetFile;
import com.temboo.Library.Dropbox.FilesAndMetadata.GetFile.GetFileInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.GetFile.GetFileResultSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.ListFolderContents;
import com.temboo.Library.Dropbox.FilesAndMetadata.ListFolderContents.ListFolderContentsInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.ListFolderContents.ListFolderContentsResultSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.SearchFilesAndFolders;
import com.temboo.Library.Dropbox.FilesAndMetadata.SearchFilesAndFolders.SearchFilesAndFoldersInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.SearchFilesAndFolders.SearchFilesAndFoldersResultSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileResultSet;
import com.temboo.Library.Dropbox.OAuth.FinalizeOAuth;
import com.temboo.Library.Dropbox.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Dropbox.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Dropbox.OAuth.InitializeOAuth;
import com.temboo.Library.Dropbox.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Dropbox.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class DropboxMover {

	public static void main(String[] args) throws TembooException, JSONException {
		Scanner in = new Scanner(System.in);
		JsonParser jsonParser = new JsonParser();
		String app_secret = "yebvxz44nxy0qcz";
		String app_key = "1vtt3z4ly9znv77";
		String callbackID;
		String oauth_token_secret;
		String access_token;
		String access_token_secret;
		String LISTFILENAME = "__list.txt";

		TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");

		
		//=================== INITIALIZE OAUTH ================== 
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		
		InitializeOAuth initializeOAuthChoreo = new InitializeOAuth(session);

		// Get an InputSet object for the choreo 
		InitializeOAuthInputSet initializeOAuthInputs = initializeOAuthChoreo.newInputSet();
		
		// Set inputs
		initializeOAuthInputs.set_DropboxAppSecret("yebvxz44nxy0qcz");
		initializeOAuthInputs.set_DropboxAppKey("1vtt3z4ly9znv77");
		
		// Execute Choreo 
		InitializeOAuthResultSet initializeOAuthResults = initializeOAuthChoreo.execute(initializeOAuthInputs); 
		
		//==================== ASK FOR ACCESS ====================
		System.out.println("Visit the following website and click Allow, pressing enter here after you have done so.\n" + initializeOAuthResults.get_AuthorizationURL()); 
		callbackID = in.nextLine().trim(); 
		callbackID = initializeOAuthResults.get_CallbackID().trim(); 
		oauth_token_secret = initializeOAuthResults.get_OAuthTokenSecret();
		
		
		//==================== FINALIZE OAUTH ==================== 
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg: 
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");
		FinalizeOAuth finalizeOAuthChoreo = new FinalizeOAuth(session);
		
		// Get an InputSet object for the choreo 
		FinalizeOAuthInputSet finalizeOAuthInputs = finalizeOAuthChoreo.newInputSet();
		
		// Set inputs 
		finalizeOAuthInputs.set_CallbackID(callbackID);
		finalizeOAuthInputs.set_DropboxAppSecret(app_secret);
		finalizeOAuthInputs.set_OAuthTokenSecret(oauth_token_secret);
		finalizeOAuthInputs.set_DropboxAppKey(app_key);
		
		// Execute Choreo 
		FinalizeOAuthResultSet finalizeOAuthResults = finalizeOAuthChoreo.execute(finalizeOAuthInputs);
		
		access_token = finalizeOAuthResults.get_AccessToken();
		access_token_secret = finalizeOAuthResults.get_AccessTokenSecret();

		
		// ==================== HARDCODING AFTER OAUTH =====================
		// System.out.println("access token " + access_token);
		// System.out.println("access token secret " + access_token_secret);
		// System.out.println("callback id " + callbackID);
		// System.out.println("oauth token secret " + oauth_token_secret);
		access_token = "xx5ylb7ssw1j8hid";
		access_token_secret = "bfwmuq8hhnqd9jx";
		callbackID = "f97f155a-b377-4635-87d3-ce7e9c61c43e";
		oauth_token_secret = "m6mX5NbI04OS2A44";

		// ==================== SEARCH FOR FILES AND FOLDERS ========================
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");

		SearchFilesAndFolders searchFilesAndFoldersChoreo = new SearchFilesAndFolders(session);

		// Get an InputSet object for the choreo
		SearchFilesAndFoldersInputSet searchFilesAndFoldersInputs = searchFilesAndFoldersChoreo.newInputSet();

		// Set inputs
		searchFilesAndFoldersInputs.set_AppSecret(app_secret);
		searchFilesAndFoldersInputs.set_AccessToken(access_token);
		searchFilesAndFoldersInputs.set_Query("move");
		searchFilesAndFoldersInputs.set_AccessTokenSecret(access_token_secret);
		searchFilesAndFoldersInputs.set_AppKey(app_key);

		// Execute Choreo
		SearchFilesAndFoldersResultSet searchFilesAndFoldersResults = searchFilesAndFoldersChoreo
				.execute(searchFilesAndFoldersInputs);
		JsonArray search_results_arr = (JsonArray) jsonParser.parse(searchFilesAndFoldersResults.get_Response());

		
		// ======================= CHECK IF FOLDER EXISTS =================
		int dir_index = 0;
		if (search_results_arr.size() == 0) {
			System.out.println("Directory 'move' must exist");
			System.exit(0);
		} else {
			boolean is_dir = false;
			for (int i = 0; i < search_results_arr.size(); i++) {
				is_dir = search_results_arr.get(i).getAsJsonObject().get("is_dir").getAsBoolean();
				if (is_dir) {
					dir_index = i;
					i = search_results_arr.size();
				}
			}
			if (!is_dir) {
				System.out.println("Directory 'move' must exist");
				System.exit(0);
			}
		}

		// If director exists, get path
		String move_folder_path = search_results_arr.get(dir_index).getAsJsonObject().get("path").getAsString();

		// ========================== LIST CONTENTS OF FOLDER ==========================
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");

		ListFolderContents listFolderContentsChoreo = new ListFolderContents(session);

		// Get an InputSet object for the choreo
		ListFolderContentsInputSet listFolderContentsInputs = listFolderContentsChoreo.newInputSet();

		// Set inputs
		listFolderContentsInputs.set_Folder(move_folder_path);
		listFolderContentsInputs.set_AppSecret(app_secret);
		listFolderContentsInputs.set_AccessToken(access_token);
		listFolderContentsInputs.set_AccessTokenSecret(access_token_secret);
		listFolderContentsInputs.set_List(true);
		listFolderContentsInputs.set_AppKey(app_key);

		// Execute Choreo
		ListFolderContentsResultSet listFolderContentsResults = listFolderContentsChoreo
				.execute(listFolderContentsInputs);

		JsonObject folder_contents_obj = (JsonObject) jsonParser.parse(listFolderContentsResults.get_Response());
		JsonArray folder_contents_arr = folder_contents_obj.get("contents").getAsJsonArray();

		String list_file_path = null;
		int list_index = 0;
		boolean list_found = false;
		for (int i = 0; i < folder_contents_arr.size(); i++) {
			list_file_path = folder_contents_arr.get(i).getAsJsonObject().get("path").getAsString();
			// Regular expression to match list text file
			// String expected_list_path = "/move/__list.[a-zA-Z]*";
			String expected_list_path = "/move/" + LISTFILENAME;
			if (list_file_path.matches(expected_list_path)) {
				list_index = i;
				list_found = true;
				i = folder_contents_arr.size();
			}
		}
		if (!list_found) {
			System.out.println("'move' folder must include a '" + LISTFILENAME + "' file");
			System.exit(0);
		}

		// ===================== READ FILE FROM FILEPATH (GETFILE) =====================
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");

		GetFile getListChoreo = new GetFile(session);

		// Get an InputSet object for the choreo
		GetFileInputSet getListInputs = getListChoreo.newInputSet();

		// Set inputs
		getListInputs.set_AccessToken(access_token);
		getListInputs.set_AppSecret(app_secret);
		getListInputs.set_AccessTokenSecret(access_token_secret);
		getListInputs.set_AppKey(app_key);
		getListInputs.set_Path(list_file_path);

		// Execute Choreo
		GetFileResultSet getListResults = getListChoreo.execute(getListInputs);

		byte[] decoded_list = Base64.decodeBase64(getListResults.get_Response());
		String[] list_contents_arr = null;
		try {
			String file_contents = new String(decoded_list, "UTF-8") + "\n";

			list_contents_arr = file_contents.split("\n");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String[] TOMOVE_file_names = new String[list_contents_arr.length];
		String[] TOMOVE_targets = new String[list_contents_arr.length];
		for (int i = 0; i < list_contents_arr.length; i++) {
			// Regex splits the file name from the location to be moved
			String[] temp_files_and_loc = list_contents_arr[i].split(" (?=/)");
			// Split to two seperate arrays, one for file name
			// One for target location
			TOMOVE_file_names[i] = temp_files_and_loc[0].trim();
			TOMOVE_targets[i] = temp_files_and_loc[1].trim();

			System.out.println(TOMOVE_file_names[i] + "   " + TOMOVE_targets[i]);
		}

		// ================== READ AND CREATE FILES ======================
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");

		GetFile getFileChoreo = new GetFile(session);

		// Get an InputSet object for the choreo
		GetFileInputSet getFileInputs = getFileChoreo.newInputSet();

		// Set inputs
		getFileInputs.set_AccessToken(access_token);
		getFileInputs.set_AppSecret(app_secret);
		getFileInputs.set_AccessTokenSecret(access_token_secret);
		getFileInputs.set_AppKey(app_key);
		String from_path;
		String to_path;

		// Looping through all target files
		for (int i = 0; i < TOMOVE_targets.length; i++) {
			from_path = "/move/" + TOMOVE_file_names[i];
			getFileInputs.set_Path(from_path);

			// Execute Choreo
			GetFileResultSet getFileResults = getFileChoreo.execute(getFileInputs);

			byte[] decoded_file = Base64.decodeBase64(getFileResults.get_Response());
			String[] file_contents_arr = null;

			try {
				String file_contents = new String(decoded_file, "UTF-8") + "\n";

				file_contents_arr = file_contents.split("\n");

			} catch (UnsupportedEncodingException e) {
				System.out.println("Couldn't read " + TOMOVE_file_names[i]);
				e.printStackTrace();
			}
			write_file(TOMOVE_file_names[i], TOMOVE_targets[i], file_contents_arr);
		}

		//======================= REMOVING ORIGINAL LIST FILE ======================== 
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg: 
		// TembooSessionsession = new TembooSession("ajm482", "CS275MoverAJM482","sIpirQut2SAsaV35c8kdRIzjkL2KG3o0");
		DeleteFileOrFolder deleteFileOrFolderChoreo = new DeleteFileOrFolder(session);
		
		// Get an InputSet object for the choreo 
		DeleteFileOrFolderInputSet deleteFileOrFolderInputs = deleteFileOrFolderChoreo.newInputSet();
		
		// Set inputs 
		deleteFileOrFolderInputs.set_AppSecret(app_secret);
		deleteFileOrFolderInputs.set_AccessToken(access_token);
		deleteFileOrFolderInputs.set_AccessTokenSecret(access_token_secret);
		deleteFileOrFolderInputs.set_AppKey(app_key);
		deleteFileOrFolderInputs.set_Path(list_file_path);
		
		// Execute Choreo 
		DeleteFileOrFolderResultSet deleteFileOrFolderResults = deleteFileOrFolderChoreo.execute(deleteFileOrFolderInputs);
		
		
		//====================== UPLOADING NEW LIST FILE ============================ 
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg: 
		// TembooSession session = new TembooSession("ajm482", "CS275MoverAJM482", "sIpirQut2SAsaV35c8kdRIzjkL2KG3o0"); 
		UploadFile uploadFileChoreo = new UploadFile(session);
		
		// Get an InputSet object for the choreo 
		UploadFileInputSet uploadFileInputs = uploadFileChoreo.newInputSet();
		
		// Set inputs 
		uploadFileInputs.set_Folder("/move");
		uploadFileInputs.set_AccessToken(access_token);
		uploadFileInputs.set_AppSecret(app_secret);
		uploadFileInputs.set_AccessTokenSecret(access_token_secret);
		uploadFileInputs.set_AppKey(app_key);
		
		String new_file_name = LISTFILENAME; 
		String new_file_contents = " ";
		uploadFileInputs.set_FileName(new_file_name);
		uploadFileInputs.set_FileContents(new_file_contents);
		
		// Execute Choreo 
		UploadFileResultSet uploadFileResults = uploadFileChoreo.execute(uploadFileInputs);
		
		in.close();
	}

	public static void write_file(String filename, String target_path, String[] file_contents_arr) {
		PrintWriter out = null;
		
		String new_path = target_path + File.separator + filename;
		System.out.println("CONCATINATING PATH AND FILENAME");
		System.out.println(target_path + "/" + filename);
		System.out.println(new_path);

		File f = new File(target_path + "/" + filename);

		if (f.exists()) {
			// If file exists, don't overwrite
			System.out.println(filename + "already exists in " + target_path);
		} else {
			// If file doesn't exist, create new and write to file
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
				
				out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
				for (int i = 0; i < file_contents_arr.length; i++) {
					out.println(file_contents_arr[i].trim());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.flush();
				out.close();
			}
		}
	}
}
