package freshwork;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import org.json.simple.*;
import org.json.simple.parser.*;

public class FreshWork {

    private static String CurrentFilePath;
    private static String fileloc;

    public static void main(String args[]) throws IOException {

	CurrentFilePath = new File(".").getCanonicalPath();
	Scanner sc = new Scanner(System.in);
	log("Do you want to change file Directory? (yes/no)");
	String result = sc.nextLine();
	if (result.equals("yes") || result.equals("YES")) {
	    openDialog();
	}
	int option = 0;
	do {
	    LOOP:
	    log("");
	    log("***************************************************************");
	    log("1.Create 2.Read 3.Delete 4.DirectoryPath 5.Exit");
	    log("***************************************************************");
	    log("Enter an Option");

	    if (sc.hasNextInt()) {

		option = sc.nextInt();

		switch (option) {
		    case 1:
			log("Please Enter Key");
			sc.nextLine();
			String key = sc.nextLine();
			if (!CheckKey(key) && !VerifyString(key)) {
			    JSONObject obj = new JSONObject();
			    log("Please Enter JsonObject Key");
			    String jsonkey = sc.nextLine();
			    log("Please Enter JsonObject Value");
			    String jsonValue = sc.nextLine();
			    obj.put(jsonkey, jsonValue);
			    Create(key, obj);
			} else {
			    log("Entered Key is Already used Or Not a valid String");
			    break;
			}
			break;
		    case 2:
			log("Please Enter Key to Read");
			sc.nextLine();
			String readkey = sc.nextLine();
			if (CheckKey(readkey)) {
			    ReadJsonFile(readkey);
			} else {
			    log("No file found with key: " + readkey);
			    break;
			}
			break;
		    case 3:
			log("Please Enter Key to delete");
			sc.nextLine();
			String deletekey = sc.nextLine();
			if (CheckKey(deletekey)) {
			    if (Delete(deletekey)) {
				log("Your object is deleted Successfully of key: " + deletekey);
			    } else {
				log("No file found with key: " + deletekey);
			    }
			} else {
			    log("No file found with key: " + deletekey);
			    break;
			}
			break;
		    case 4:
			log("Directory: " + CurrentFilePath);
			break;
		    case 5:
			log("Thank you FreshWork for using my program");
			break;
		    default:
			log("Please Enter Valid Choice");
			break;
		}
	    } else {
		log("Please Enter Valid Input Option");
		break;
	    }
	} while (option != 5);
    }

    private static void Create(String Key, JSONObject Value) {
	JSONObject obj = new JSONObject();
	obj.put(Key, Value);

	JSONArray array = new JSONArray();
	array.add(obj);
	WriteJsonFile(Key, array);
    }

    private static void openDialog() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int result = fileChooser.showOpenDialog(null);
	if (result == JFileChooser.APPROVE_OPTION) {
	    File selectedFile = fileChooser.getSelectedFile();
	    CurrentFilePath = selectedFile.toString();
	    log(CurrentFilePath);
	}
    }

    private static void WriteJsonFile(String FileName, JSONArray array) {
	fileloc = CurrentFilePath + "\\Data_" + FileName + ".json";
	try (FileWriter file = new FileWriter(fileloc)) {
	    file.write(array.toJSONString());
	    file.flush();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static void ReadJsonFile(String key) {

	JSONParser jsonParser = new JSONParser();
	String path = CurrentFilePath + "\\Data_" + key + ".json";
	try (FileReader reader = new FileReader(path)) {

	    Object obj = jsonParser.parse(reader);
	    JSONArray array = (JSONArray) obj;
	    System.out.println(array);

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    private static boolean CheckKey(String key) {
	String path = CurrentFilePath + "\\Data_" + key + ".json";
	File f = new File(path);
	return f.exists();
    }

    private static boolean Delete(String key) {
	String path = CurrentFilePath + "\\Data_" + key + ".json";
	File f = new File(path);
	return f.delete();
    }

    private static void log(String string) {
	System.out.println(string);
    }

    private static boolean VerifyString(String key) {
	Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	Matcher m = p.matcher(key);
	return m.find();
    }
}
