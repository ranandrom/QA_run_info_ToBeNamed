package QA_run_info;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Data_Aggregation.DataAggregation;
import SubThread.SubThread;

/**
 * Description: <br/>
 * Function: This class is main class. <br/>
 * File Name: QA_run_info.java <br/>
 * Date: 2017-03-30
 * 
 * @author Luzhirong ramandrom@139.com
 * @version V1.0.0
 */

public class QA_run_info
{
	/**
	 * main��������������.
	 * 
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException
	{
		System.out.println();
		Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_star = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String day = formatter.format(now_star.getTime()); // ��ʽ���������

		int args_len = args.length; // ϵͳ�����������Ĳ�������
		int Cover = 1; // 0�����ǻ��ܱ�1����׷��
		int Uploadtag = 0; // 0�������б��ϴ���1����ֻ�ϴ����±�
		int Upload = 1; // �����Ƿ���Ҫ�ϴ���/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/��0�����ϴ���1�����ϴ�
		String dir = "./Ironman"; // ������·��
		String ExcelFormat = "xlsx"; // Excel���ʽ��׺
		String Input = "/Src_Data1/analysis/Ironman/"; // ����Ŀ��·��
		String PutPath = "/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/" + day; // �ϴ��ļ�����/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/���µ��½�Ŀ¼
		String Path = null; // ��Ŀ����ļ�����·��

		int logp = 0; // "-p"����������������־
		int logc = 0; // "-c"����������������־
		int logo = 0; // "-o"����������������־
		int logf = 0; // "-f"����������������־
		int logu = 0; // "-u"����������������־
		int logl = 0; // "-u"����������������־
		for (int len = 0; len < args_len; len += 2) {
			if (args[len].equals("-P") || args[len].equals("-p")) {
				Input = args[len + 1];
				logp++;
			} else if (args[len].equals("-C") || args[len].equals("-c")) {
				Cover = Integer.valueOf(args[len + 1]);
				logc++;
			} else if (args[len].equals("-O") || args[len].equals("-o")) {
				dir = args[len + 1];
				logo++;
			} else if (args[len].equals("-F") || args[len].equals("-f")) {
				Uploadtag = Integer.valueOf(args[len + 1]);
				logf++;
			} else if (args[len].equals("-U") || args[len].equals("-u")) {
				Upload = Integer.valueOf(args[len + 1]);
				logu++;
			} else if (args[len].equals("-L") || args[len].equals("-l")) {
				PutPath = args[len + 1];
				;
				logl++;
			} else if ((args_len == 1) && args[0].equals("-help")) {
				System.out.println();
				System.out.println("Version: V1.0.0");
				System.out.println();
				System.out.println("Usage:\t java -jar QA_run_info.jar [Options] [args...]");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-P or -p\t Set operation path. The default value is \"/Src_Data1/analysis/Ironman/\".");
				System.out.println(
						"-C or -c\t Set Whether cover old file. Inuput 0 or 1, 0 representative overwrite file data and 1 additional file data. The default value is 1.");
				System.out.println("-O or -o\t Set output file. The default value is \"./Ironman\".");
				System.out.println(
						"-U or -u\t Set Whether upload file to wdmycloud. Inuput 0 or 1, 1 representative upload file and 0 is not. The default value is 1.");
				System.out.println(
						"-F or -f\t Set file upload pattern(all file or partial file). Inuput 0 or 1, 0 representative upload all file and 1 upload partial file. The default value is 0.");
				System.out.println(
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/\".");
				System.out.println();
				return;
			} else {
				System.out.println();
				System.out.println("�Բ����������Options�����ڣ�����ȱ�������������������²�����ʾ���룡");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-P or -p\t Set operation path. The default value is \"/Src_Data1/analysis/Ironman/\".");
				System.out.println(
						"-C or -c\t Set Whether cover old file. Inuput 0 or 1, 0 representative overwrite file data and 1 additional file data. The default value is 1.");
				System.out.println("-O or -o\t Set output file. The default value is \"./Ironman\".");
				System.out.println(
						"-U or -u\t Set Whether upload file to wdmycloud. Inuput 0 or 1, 1 representative upload file and 0 is not. The default value is 1.");
				System.out.println(
						"-F or -f\t Set file upload pattern(all file or partial file). Inuput 0 or 1, 0 representative upload all file and 1 upload partial file. The default value is 0.");
				System.out.println(
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/\".");
				System.out.println();
				return;
			}
			if (logp > 1 || logc > 1 || logo > 1 || logf > 1 || logu > 1 || logl > 1) {
				System.out.println();
				System.out.println("�Բ����������Options���ظ�����������²�����ʾ���룡");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-P or -p\t Set operation path. The default value is \"/Src_Data1/analysis/Ironman/\".");
				System.out.println(
						"-C or -c\t Set Whether cover old file. Inuput 0 or 1, 0 representative overwrite file data and 1 additional file data. The default value is 1.");
				System.out.println("-O or -o\t Set output file. The default value is \"./Ironman\".");
				System.out.println(
						"-U or -u\t Set Whether upload file to wdmycloud. Inuput 0 or 1, 1 representative upload file and 0 is not. The default value is 1.");
				System.out.println(
						"-F or -f\t Set file upload pattern(all file or partial file). Inuput 0 or 1, 0 representative upload all file and 1 upload partial file. The default value is 0.");
				System.out.println(
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/��ӨӨ/��Ŀ-����-���ܱ�/\".");
				System.out.println();
				return;
			}
		}

		System.out.println("����ʼʱ��: " + formatter_star.format(now_star.getTime()));
		System.out.println("===============================================");
		System.out.println("QA_run_info.1.0.0");
		System.out.println("***********************************************");
		System.out.println();

		String Data_Aggregation_Path = dir + "/Data_Aggregation/";
		File DAP = new File(Data_Aggregation_Path);
		if (Cover == 1) {
			if (DAP.exists() && DAP.isDirectory()) {
				Copy_Old_File(Data_Aggregation_Path); // ����ָ��Ŀ¼���������ڵ��ļ�
			} else {
				System.out.println(Data_Aggregation_Path + "Ŀ¼������");
			}
		}

		File fileInput = new File(Input);
		ExecutorService exe = Executors.newFixedThreadPool(15); // �����̳߳�����߳���Ϊ15

		int Input_length = 0;
		String InputArr[] = Input.split("/");
		for (int i = 0; i < InputArr.length; i++) {
			if (InputArr[InputArr.length - 1].equals("Ironman")) {
				Input_length = 0;
			} else if (InputArr[InputArr.length - 2].equals("Ironman")) {
				Input_length = 1;
			} else if (InputArr[InputArr.length - 3].equals("Ironman")) {
				Input_length = 2;
			}
		}
		if (Input_length == 0) {
			Path = dir;
			for (File pathname : fileInput.listFiles()) {
				exe.execute(new SubThread(dir, pathname, ExcelFormat, Input_length));
			}
		} else if (Input_length == 1) {
			Path = dir + "/" + InputArr[InputArr.length - 1];
			exe.execute(new SubThread(dir, fileInput, ExcelFormat, Input_length));

		} else if (Input_length == 2) {
			Path = dir + "/" + InputArr[InputArr.length - 2] + "/" + InputArr[InputArr.length - 1];
			exe.execute(new SubThread(dir, fileInput, ExcelFormat, Input_length));
		} else {
			System.out.println(Input + "�ǷǷ����룬���������룡");
			return;
		}
		exe.shutdown(); // �ر��̳߳�
		while (true) {
			if (exe.isTerminated()) { // �������е����߳������꣬���������߳�
				DataAggregation.outPutData(dir + "/Data_Aggregation/" + day, Path, Cover, PutPath, Uploadtag, Upload); // ���ݻ���
				break;
			}
			Thread.sleep(500);
		}

		Thread.sleep(3000);
		uploadFileToFront(dir); // �ϴ��ļ��������ƶ�

		Calendar now_end = Calendar.getInstance();
		SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println();
		System.out.println("==============================================");
		System.out.println("�������ʱ��: " + formatter_end.format(now_end.getTime()));
		System.out.println();
	}

	/**
	 * ����ָ��Ŀ¼���������ڵ��ļ�
	 * 
	 * @param Path
	 */
	@SuppressWarnings("unused")
	public static void Copy_Old_File(String Path)
	{
		Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		String Day = formatter_Date.format(now_star.getTime());
		String daynum = getNewFilePAth(Path); // ��ȡָ��Ŀ¼����������������Ŀ¼��
		String cmd1 = "find " + Path + daynum + " -type f -name *" + daynum + "*.xlsx";
		my_mkdir(Path + "/" + Day); // ������������������Ŀ¼
		try {
			Process process = Runtime.getRuntime().exec(cmd1);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				File pathname = new File(line);
				String file_name = pathname.getName();
				String Suffix = file_name.substring(file_name.lastIndexOf(".")); // ��ȡ��׺��
				String Remove_suffix = file_name.replaceAll(Suffix, ""); // ȥ����׺��
				String Arr[] = Remove_suffix.split("_");
				String newname = null;
				for (int i = 0; i < Arr.length - 1; i++) {
					if (i == 0) {
						newname = Arr[i];
					} else {
						newname += "_" + Arr[i];
					}
				}
				String cmd2 = "cp " + line + " " + Path + "/" + Day + "/" + newname + "_" + Day + ".xlsx";
				Process process2 = Runtime.getRuntime().exec(cmd2);
				BufferedReader input2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
				String line2 = null;
				while ((line2 = input2.readLine()) != null) { // ѭ������ϵͳ�������ݣ���֤ϵͳ�����Ѿ���������
					// System.out.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡָ��Ŀ¼����������������Ŀ¼��
	 * 
	 * @param Path
	 * @return String
	 */
	public static String getNewFilePAth(String Path)
	{
		File file = new File(Path);
		int daynum = 0;
		for (File dir : file.listFiles()) {
			if (dir.isDirectory()) { // �����Ŀ¼
				String dir_name = dir.getName(); // Ŀ¼��
				if (daynum < Integer.valueOf(dir_name)) {
					daynum = Integer.valueOf(dir_name);
				} else {
					continue;
				}
			} else {
				continue;
			}
		}
		return String.valueOf(daynum);
	}

	/**
	 * ����Ŀ¼�ķ�����
	 * 
	 * @param dir_name
	 */
	public static void my_mkdir(String dir_name)
	{
		File file = new File(dir_name);
		// ����ļ������ڣ��򴴽�
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	/**
	 * ���ýű����ϴ�ָ��Ŀ¼�µ��ļ����ƶ�
	 * 
	 * @param PutPath
	 */
	@SuppressWarnings("unused")
	public static void uploadFileToFront(String PutPath)
	{
		String cmd = "/opt/local/bin/python35/python /var/script/alan/10k_api_script/qa_run_info_collections.py -path "
				+ PutPath;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) { // ѭ������ϵͳ�������ݣ���֤ϵͳ�����Ѿ���������
				// System.out.println(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
