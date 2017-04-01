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
	 * main方法，程序的入口.
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
		String day = formatter.format(now_star.getTime()); // 格式化后的日期

		int args_len = args.length; // 系统传入主函数的参数长度
		int Cover = 1; // 0代表覆盖汇总表，1代表追加
		int Uploadtag = 0; // 0代表所有表上传，1代表只上传更新表
		int Upload = 1; // 设置是否需要上传至/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/，0代表不上传，1代表上传
		String dir = "./Ironman"; // 输出结果路径
		String ExcelFormat = "xlsx"; // Excel表格式后缀
		String Input = "/Src_Data1/analysis/Ironman/"; // 操作目标路径
		String PutPath = "/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/" + day; // 上传文件到“/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/”下的新建目录
		String Path = null; // 项目结果文件查找路径

		int logp = 0; // "-p"参数输入次数计算标志
		int logc = 0; // "-c"参数输入次数计算标志
		int logo = 0; // "-o"参数输入次数计算标志
		int logf = 0; // "-f"参数输入次数计算标志
		int logu = 0; // "-u"参数输入次数计算标志
		int logl = 0; // "-u"参数输入次数计算标志
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
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/\".");
				System.out.println();
				return;
			} else {
				System.out.println();
				System.out.println("对不起，您输入的Options不存在，或者缺少所需参数，请参照以下参数提示输入！");
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
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/\".");
				System.out.println();
				return;
			}
			if (logp > 1 || logc > 1 || logo > 1 || logf > 1 || logu > 1 || logl > 1) {
				System.out.println();
				System.out.println("对不起，您输的入Options有重复，请参照以下参数提示输入！");
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
						"-L or -l\t Set Upload file path. The default value is \"/wdmycloud/anchordx_cloud/杨莹莹/项目-生信-汇总表/\".");
				System.out.println();
				return;
			}
		}

		System.out.println("程序开始时间: " + formatter_star.format(now_star.getTime()));
		System.out.println("===============================================");
		System.out.println("QA_run_info.1.0.0");
		System.out.println("***********************************************");
		System.out.println();

		String Data_Aggregation_Path = dir + "/Data_Aggregation/";
		File DAP = new File(Data_Aggregation_Path);
		if (Cover == 1) {
			if (DAP.exists() && DAP.isDirectory()) {
				Copy_Old_File(Data_Aggregation_Path); // 复制指定目录下最新日期的文件
			} else {
				System.out.println(Data_Aggregation_Path + "目录不存在");
			}
		}

		File fileInput = new File(Input);
		ExecutorService exe = Executors.newFixedThreadPool(15); // 设置线程池最大线程数为15

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
			System.out.println(Input + "是非法输入，请重新输入！");
			return;
		}
		exe.shutdown(); // 关闭线程池
		while (true) {
			if (exe.isTerminated()) { // 先让所有的子线程运行完，再运行主线程
				DataAggregation.outPutData(dir + "/Data_Aggregation/" + day, Path, Cover, PutPath, Uploadtag, Upload); // 数据汇总
				break;
			}
			Thread.sleep(500);
		}

		Thread.sleep(3000);
		uploadFileToFront(dir); // 上传文件到阿里云端

		Calendar now_end = Calendar.getInstance();
		SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println();
		System.out.println("==============================================");
		System.out.println("程序结束时间: " + formatter_end.format(now_end.getTime()));
		System.out.println();
	}

	/**
	 * 复制指定目录下最新日期的文件
	 * 
	 * @param Path
	 */
	@SuppressWarnings("unused")
	public static void Copy_Old_File(String Path)
	{
		Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		String Day = formatter_Date.format(now_star.getTime());
		String daynum = getNewFilePAth(Path); // 获取指定目录下最新日期命名的目录名
		String cmd1 = "find " + Path + daynum + " -type f -name *" + daynum + "*.xlsx";
		my_mkdir(Path + "/" + Day); // 创建当天日期命名的目录
		try {
			Process process = Runtime.getRuntime().exec(cmd1);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				File pathname = new File(line);
				String file_name = pathname.getName();
				String Suffix = file_name.substring(file_name.lastIndexOf(".")); // 获取后缀名
				String Remove_suffix = file_name.replaceAll(Suffix, ""); // 去除后缀名
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
				while ((line2 = input2.readLine()) != null) { // 循环读出系统返回数据，保证系统调用已经正常结束
					// System.out.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定目录下最新日期命名的目录名
	 * 
	 * @param Path
	 * @return String
	 */
	public static String getNewFilePAth(String Path)
	{
		File file = new File(Path);
		int daynum = 0;
		for (File dir : file.listFiles()) {
			if (dir.isDirectory()) { // 如果是目录
				String dir_name = dir.getName(); // 目录名
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
	 * 创建目录的方法。
	 * 
	 * @param dir_name
	 */
	public static void my_mkdir(String dir_name)
	{
		File file = new File(dir_name);
		// 如果文件不存在，则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	/**
	 * 调用脚本，上传指定目录下的文件到云端
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
			while ((line = input.readLine()) != null) { // 循环读出系统返回数据，保证系统调用已经正常结束
				// System.out.println(line);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
