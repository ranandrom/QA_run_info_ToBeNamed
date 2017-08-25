package SubThread;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;


/**
 * <br/>
 * Function: This class is Thread class. <br/>
 * File Name: SubThread.java <br/>
 * Date: 2017-03-30
 * 
 * @author Luzhirong ramandrom@139.com
 * @version V1.0.0
 */

public class SubThread extends Thread
{
	private String dir;
	private File pathname;
	private String ExcelFormat;
	private int inputlenght;

	/**
	 * SubThread类的构造器。
	 * 
	 * @param dir
	 * @param pathname
	 * @param ExcelFormat
	 * @param inputlenght
	 */
	public SubThread(String dir, File pathname, String ExcelFormat, int inputlenght) {
		this.dir = dir;
		this.pathname = pathname;
		this.ExcelFormat = ExcelFormat;
		this.inputlenght = inputlenght;
	}

	/**
	 * 重写的线程类run方法。
	 */
	public void run()
	{
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter_Date = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat formatter_end = new SimpleDateFormat("HH_mm_ss");
		String Day = formatter_Date.format(now.getTime());
		String time = formatter_end.format(now.getTime());
		
		if (inputlenght == 0 || inputlenght == 1) {
			if (pathname.isDirectory()) { // 如果是目录
				String dir_name = pathname.getName(); // 目录名
				String InputArr[] = dir_name.split("_");
				if (InputArr.length == 4) {
					if(InputArr[0].length() == 6 && Regular_Expression(InputArr[0], "^[0-2]\\d[0-1]\\d[0-3]\\d") != null){
						if(Regular_Expression(InputArr[1], "^[A-Z]{1,}\\d{1,}") != null){
							if(InputArr[2].length() == 4 && Regular_Expression(InputArr[2], "^\\d{4}") != null){
								if(Regular_Expression(InputArr[3], "^[A-Z0-9]{1,}") != null){
									String Sequencing_Info = dir + "/" + dir_name;
									if (!(dir_name.startsWith("0."))) {
										for (File porject : pathname.listFiles()) {
											if (porject.isDirectory()) { // 如果是目录
												// 获取文件的绝对路径
												String Folder = porject.getParent();
												String Por_name = porject.getName(); // 子目录名
												String Path = Folder + "/" + Por_name;
												String Por_dir = Sequencing_Info + "/" + Por_name;
												String Plasma_ExcelName = "QA_run_info_" + Por_name + "_Plasma_" + Day + "." + ExcelFormat; // 血浆表
												String Tissue_ExcelName = "QA_run_info_" + Por_name + "_Tissue_" + Day + "." + ExcelFormat; // 组织表
												String BC_ExcelName = "QA_run_info_" + Por_name + "_BC_" + Day + "." + ExcelFormat; // 白细胞表
												String Test_ExcelName = "QA_run_info_" + Por_name + "_Test_" + Day + "."
														+ ExcelFormat; // 测试数据表
												String Plasma_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Plasma_" + Day + ".tsv"; // 血浆tsv文件
												String Tissue_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Tissue_" + Day + ".tsv"; // 组织tsv文件
												String BC_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_BC_" + Day + ".tsv"; // 白细胞tsv文件
												String Test_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Test_" + Day + ".tsv"; // 测试数据tsv文件
												String Plasma_Excel = Por_dir + "/" + Plasma_ExcelName;
												String Tissue_Excel = Por_dir + "/" + Tissue_ExcelName;
												String BC_Excel = Por_dir + "/" + BC_ExcelName;
												String Test_Excel = Por_dir + "/" + Test_ExcelName;
												File Plasma_excel = new File(Plasma_Excel);
												File Tissue_excel = new File(Tissue_Excel);
												File BC_excel = new File(BC_Excel);
												File Test_excel = new File(Test_Excel);
												my_mkdir(Por_dir);

												createXlsx(Plasma_excel); // 创建血浆表
												createXlsx(Tissue_excel); // 创建组织表
												createXlsx(BC_excel); // 创建测试表
												createXlsx(Test_excel); // 测试的数据表

												calculatedData(Plasma_Excel, Tissue_Excel, BC_Excel, Test_Excel, Plasma_Tsv, Tissue_Tsv, BC_Tsv,
														Test_Tsv, Path, Por_name);
												
											} else {
												continue;
											}
										}
									}
								} else {
									return;
								}
							} else {
								return;
							}
						} else {
							return;
						}
					} else {
						return;
					}
				} else {
					return;
				}
			}
		} else if (inputlenght == 2) {
			// 获取文件的绝对路径
			String Folder = pathname.getParent(); // 父目录
			String Foldername = new File(Folder).getName(); // 父目录名
			String Por_name = pathname.getName(); // 子目录名
			String Path = Folder + "/" + Por_name;
			String Por_dir = dir + "/" + Foldername + "/" + Por_name;
			String Plasma_ExcelName = "QA_run_info_" + Por_name + "_Plasma_" + Day + "." + ExcelFormat; // 血浆表
			String Tissue_ExcelName = "QA_run_info_" + Por_name + "_Tissue_" + Day + "." + ExcelFormat; // 组织表
			String BC_ExcelName = "QA_run_info_" + Por_name + "_BC_" + Day + "." + ExcelFormat; // 白细胞表
			String Test_ExcelName = "QA_run_info_" + Por_name + "_Test_" + Day + "." + ExcelFormat; // 测试数据表
			String Plasma_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Plasma_" + Day + ".tsv"; // 血浆tsv文件
			String Tissue_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Tissue_" + Day + ".tsv"; // 组织tsv文件
			String BC_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_BC_" + Day + ".tsv"; // 白细胞tsv文件
			String Test_Tsv = Por_dir + "/" + "QA_run_info_" + Por_name + "_Test_" + Day + ".tsv"; // 测试数据tsv文件
			String Plasma_Excel = Por_dir + "/" + Plasma_ExcelName;
			String Tissue_Excel = Por_dir + "/" + Tissue_ExcelName;
			String BC_Excel = Por_dir + "/" + BC_ExcelName;
			String Test_Excel = Por_dir + "/" + Test_ExcelName;
			File Plasma_excel = new File(Plasma_Excel);
			File Tissue_excel = new File(Tissue_Excel);
			File BC_excel = new File(BC_Excel);
			File Test_excel = new File(Test_Excel);
			my_mkdir(Por_dir);

			createXlsx(Plasma_excel); // 创建血浆表
			createXlsx(Tissue_excel); // 创建组织表
			createXlsx(BC_excel); // 创建测试表
			createXlsx(Test_excel); // 测试的数据表

			calculatedData(Plasma_Excel, Tissue_Excel, BC_Excel, Test_Excel, Plasma_Tsv, Tissue_Tsv, BC_Tsv, Test_Tsv, Path, Por_name);

		}
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
	 * 新建xlsx格式文件的方法。
	 * 
	 * @param file
	 */
	@SuppressWarnings("deprecation")
	public static void createXlsx(File file)
	{
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			// 创建Excel的工作sheet,对应到一个excel文档的tab  
			XSSFSheet sheet = workbook.createSheet("sheet1");
			// 在索引0的位置创建行（最顶端的行）
			XSSFRow row0 = sheet.createRow((short) 0);

			String head_row0 = "Sample ID" + "\t" + "Pre-lib name" + "\t" + "Identification name" + "\t"
					+ "Sequencing info" + "\t" + "Sequencing file name" + "\t" + "Mapping%" + "\t" + "Total PF reads"
					+ "\t" + "Mean_insert_size" + "\t" + "Median_insert_size" + "\t" + "On target%" + "\t"
					+ "Pre-dedup mean bait coverage" + "\t" + "Deduped mean bait coverage" + "\t"
					+ "Deduped mean target coverage" + "\t" + "% target bases > 30X" + "\t" + "Uniformity (0.2X mean)"
					+ "\t" + "C methylated in CHG context" + "\t" + "C methylated in CHH context" + "\t"
					+ "C methylated in CpG context" + "\t" + "QC result" + "\t" + "Date of QC" + "\t"
					+ "Path to sorted.deduped.bam" + "\t" + "Date of path update" + "\t" + "Bait set" + "\t"
					+ "log2(CPM+1)" + "\t" + "Sample QC" + "\t" + "Failed QC Detail" + "\t" + "Warning QC Detail" + "\t"
					+ "PE report.txt(Mapping%)" + "\t" + "Pre-lib name*sorted.deduplicated.bam.perTarget.coverage(Uniformity (0.2X mean))" + "\t"
					+ "Pre-lib name*sorted.deduplicated.bam.hsmetrics.txt(Deduped mean bait coverage; Deduped mean target coverage; % target bases > 30X)" + "\t"
					+ "Pre-lib name*sorted.deduplicated.bam.insertSize.txt(Mean_insert_size; Median_insert_size)" + "\t"
					+ "Pre-lib name*sorted.bam.hsmetrics.txt(Total PF reads; On target%; Pre-dedup mean bait coverage; Bait set)" + "\t"
					+ "Pre-lib name*PE_report.txt(C methylated in CHG context; C methylated in CHH context; C methylated in CpG context)" + "\t"
					+ "Pre-lib name*hsmetrics.QC.xls*(QC result; Date of QC; Date of path update)" + "\t"
					+ "Mark" + "\t" + "Check" + "\t" + "Note1" + "\t" + "Note2" + "\t" + "Note3";

			// 1、创建字体，设置其为红色：
			XSSFFont font = workbook.createFont();
			font.setColor(HSSFFont.COLOR_RED);
			font.setFontHeightInPoints((short) 10);
			font.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 1、创建字体，设置其为粗体，背景蓝色：
			XSSFFont font1 = workbook.createFont();
			font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			font1.setFontHeightInPoints((short) 10);
			font1.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle1 = workbook.createCellStyle();
			cellStyle1.setFont(font1);
			cellStyle1.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle1.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle1.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle1.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle1.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			cellStyle1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 1、创建字体，设置其为红色、粗体，背景绿色：
			XSSFFont font2 = workbook.createFont();
			font2.setColor(HSSFFont.COLOR_RED);
			font2.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			font2.setFontHeightInPoints((short) 10);
			font2.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle2 = workbook.createCellStyle();
			cellStyle2.setFont(font2);
			cellStyle2.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle2.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle2.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			cellStyle2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 1、创建字体大小为10，背景蓝色：
			XSSFFont font3 = workbook.createFont();
			font3.setFontHeightInPoints((short) 10);
			font3.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle3 = workbook.createCellStyle();
			cellStyle3.setFont(font3);
			cellStyle3.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle3.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle3.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
			cellStyle3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 1、创建字体大小为10，背景黄色：
			XSSFFont font4 = workbook.createFont();
			font4.setFontHeightInPoints((short) 10);
			font4.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle4 = workbook.createCellStyle();
			cellStyle4.setFont(font4);
			cellStyle4.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle4.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle4.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyle4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			// 1、创建字体，设置其为粗体，背景黄色：
			XSSFFont font5 = workbook.createFont();
			font5.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			font5.setFontHeightInPoints((short) 10);
			font5.setFontName("Palatino Linotype");
			// 2、创建格式
			XSSFCellStyle cellStyle5 = workbook.createCellStyle();
			cellStyle5.setFont(font5);
			cellStyle5.setBorderTop(XSSFCellStyle.BORDER_THIN);
			cellStyle5.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			cellStyle5.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			cellStyle5.setBorderRight(XSSFCellStyle.BORDER_THIN);
			cellStyle5.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			cellStyle5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

			String str_head_row0[] = head_row0.split("\t");
			// 在单元格中输入一些内容
			for (int i = 0; i < str_head_row0.length; i++) {
				// 在索引0的位置创建单元格（左上端）
				XSSFCell cell = row0.createCell(i);
				if (i < 4) { // 实验表格的 "Sample ID" ～ "Sequencing info"：红字橘底
					cell.setCellValue(str_head_row0[i]);
					cell.setCellStyle(cellStyle2);
				} else if (str_head_row0[i].equals("Path to sorted.deduped.bam") || str_head_row0[i].equals("Date of path update")) { // "Path to sorted.deduped.bam"、"Date of path update"：黑字黄底。
					cell.setCellStyle(cellStyle5);
					cell.setCellValue(str_head_row0[i]);
				} else { // 剩下的生信表格的列：黑字蓝底
					cell.setCellStyle(cellStyle1);
					cell.setCellValue(str_head_row0[i]);
				}
			}

			// 新建一输出文件流
			FileOutputStream fOut = new FileOutputStream(file);
			// 把相应的Excel工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
			workbook.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 调用linux命令获取符合要求的文件列表(跳过链接文件)
	 * 
	 * @param Input
	 * @return List
	 */
	public static ArrayList<String> searchFile(String Input)
	{
		ArrayList<String> data_ID = new ArrayList<String>();
		ArrayList<String> data_IDP = new ArrayList<String>();
		File fileInput = new File(Input);
		try {
			String InputArr[] = Input.split("/");
			if (InputArr.length == 3) {
				for (File pathname : fileInput.listFiles()) {
					if (pathname.isDirectory()) { // 如果是目录
						String dir_name = pathname.getName(); // 子目录名
						if (!(dir_name.startsWith("0."))) {
							String cmd = "find " + Input + "/" + dir_name + " -type f"; // 查找该目录下所有文件（链接文件除外）
							Process process = Runtime.getRuntime().exec(cmd);
							BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
							String line = "";
							while ((line = input.readLine()) != null) {
								File file = new File(line);
								// 获取文件的绝对路径
								String Folder = file.getParent();
								// 把文件名（basename）添加进列表
								String FileName = file.getName();
								String regEx = "[DPM|S].*_R1_001";
								String ID = Regular_Expression(FileName, regEx);
								String IDP = ID + "\t" + Folder;
								if (data_ID.contains(ID) || ID == null) {
									continue;
								} else {
									data_ID.add(ID);
									data_IDP.add(IDP);
								}
							}
						} else {
							continue;
						}
					} else {
						continue;
					}
				}
			} else {
				String cmd = "find " + Input + " -type f"; // 查找该目录下所有文件（链接文件除外）
				Process process = Runtime.getRuntime().exec(cmd);
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = "";
				while ((line = input.readLine()) != null) {
					File file = new File(line);
					// 获取文件的绝对路径
					String Folder = file.getParent();
					// 把文件名（basename）添加进列表
					String FileName = file.getName();
					String regEx = "[DPM|S].*_R1_001";
					String ID = Regular_Expression(FileName, regEx);
					String IDP = ID + "\t" + Folder;
					if (data_ID.contains(ID) || ID == null) {
						continue;
					} else {
						data_ID.add(ID);
						data_IDP.add(IDP);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("linux命令异常！！！！");
		}
		return data_IDP;
	}

	/**
	 * 调用正则表达式的方法。
	 * 
	 * @param str
	 * @param regEx
	 * @return String
	 */
	public static String Regular_Expression(String str, String regEx)
	{
		String data = null;
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			data = matcher.group();
		}
		return data;
	}

	/**
	 * 提取Sample_ID的方法。
	 * 
	 * @param Pre_lib_name
	 * @return String
	 */
	public static String Extract_Sample_ID(String Pre_lib_name)
	{
		String str[] = Pre_lib_name.split("-");
		String strr = null;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("DPM")) {
				if (str[i + 1].equals("DNA")) {
					strr = str[i + 2] + "-" + str[i + 3];
					break;
				} else {
					strr = str[i + 1] + "-" + str[i + 2];
					break;
				}
			} else {
				continue;
			}
		}
		return strr;
	}

	/**
	 * 提取Sequencing_Info的方法。
	 * 
	 * @param inputstr
	 * @return String
	 */
	public static String Extract_Sequencing_Info(String inputstr)
	{
		String str[] = inputstr.split("/");
		String strr = null;
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals("Ironman")) {
				strr = str[i + 1];
			} else {
				continue;
			}
		}
		return strr;
	}

	/**
	 * 调用linux命令而且命令返回只有一行数据的方法
	 * 
	 * @param cmd
	 * @return String
	 */
	public static String Linux_Cmd(String[] cmd)
	{
		String line = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line = input.readLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return line;
	}

	/**
	 * 判断一个Linux下的文件是否为链接文件，是返回true ,否则返回false
	 * 
	 * @param file
	 * @return boolean
	 */
	public static boolean isLink(File file)
	{
		String cPath = null;
		try {
			cPath = file.getCanonicalPath();
		} catch (Exception e) {
			System.out.println("文件异常：" + file.getAbsolutePath());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return !cPath.equals(file.getAbsolutePath());
	}

	/**
	 * 在指定目录下查找flagstat.xls的方法。
	 * 
	 * @param Path
	 * @param Start
	 * @param End
	 * @return List
	 */
	public static ArrayList<String> Find_flagstat_xls(String Path, String Start, String End, ArrayList<String> filelist)
	{
		ArrayList<String> Data_list = new ArrayList<String>();
		String line = null;
		String data = null;
		int loog = 0;
		int filelog1 = 0;
		int filelog2 = 0;
		try {
			String cmd = "find " + Path + " -type f -name flagstat.xls";
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = input.readLine()) != null) {
				File file = new File(line);
				int log = 0;
				if (isLink(file)) {
					System.out.println("链接文件：" + line);
					continue;
				} else {
					filelist.add(line);
					String encoding = "GBK";
					InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
					BufferedReader bufferedReader = new BufferedReader(read);
					String lineTxt = null;
					while ((lineTxt = bufferedReader.readLine()) != null) {
						String str[] = lineTxt.split("\t");
						if (str[0].contains(Start) && str[0].endsWith(End)) {
							if (filelog1 == 0) {
								if (Data_list.contains(str[1])) {
									continue;
								} else {
									Data_list.add(str[1]);
								}
								filelog2++;
							} else if ((filelog1 != 0) && (filelog2 != 0)) {
								if (Data_list.contains(str[1])) {
									continue;
								} else {
									Data_list.add(str[1]);
									System.out.println(line + "异常！其符合" + Start + "*" + End + "的行的第二列的数据与另一个文件不一致！");
								}
							}
							log++;
						} else {
							continue;
						}
					}
					bufferedReader.close();
				}
				filelog1++;
				if (log == 0) {
					continue;
				} else if (log == 1) {
					loog = 1;
				} else if (log > 1) {
					loog = 1;
					System.out.println(line + "异常！包含多行" + Start + "*" + End + "的行！");
				}
			}
			if (loog == 0) {
				String cmd1 = "find " + Path + " -name " + Start + "*sorted.bam.flagstat";
				Process process1 = Runtime.getRuntime().exec(cmd1);
				BufferedReader input1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
				int i = 0;
				while ((line = input1.readLine()) != null) {
					filelist.add(line);
					String[] cmd4 = { "awk", "NR==5 {print $5 }", line };
					data = Linux_Cmd(cmd4);
					Data_list.add(data);
					i++;
				}
				if (i > 1) {
					System.out.println(Path + "目录下有" + i + "多个符合" + Start + "*sorted.bam.flagstat" + "的文件！");
				} else if (i == 0) {
					Data_list.add("NA");
				}
			}

			if (data == null) {
				Data_list.add("NA");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Data_list;
	}

	/**
	 * 获取修改时间的方法  
	 * 
	 * @param file
	 * @return String
	 */
	public static String getModifiedTime(String file)
	{
		File f = new File(file);
		Calendar cal = Calendar.getInstance();
		long time = f.lastModified();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		cal.setTimeInMillis(time);
		return formatter.format(cal.getTime()); // 返回格式化的日期
	}

	/**
	 * 删除Excel表文件里空行的方法。
	 * 
	 * @param file
	 * @return int
	 */
	public static int removeNullRow(File file)
	{
		try {
			FileInputStream is = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheetAt(0); // 获取第1个工作薄

			// 获取当前工作薄的每一行
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				XSSFRow xssfrow = sheet.getRow(i);
				if (xssfrow == null || (checkRowNull(xssfrow) == 0)) {
					System.out.println("删除空行：" + i);
					sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);

					// 新建一输出文件流
					FileOutputStream fOut = new FileOutputStream(file);
					// 把相应的Excel 工作簿存盘
					workbook.write(fOut);
					fOut.flush();
					// 操作结束，关闭文件
					fOut.close();
					is.close();
					workbook.close();
					return 1;
				} else {
					continue;
				}
			}
			is.close();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 判断指定行是否为空,如果为空，则返回0
	 * 
	 * @param xssfRow
	 * @return int
	 */
	@SuppressWarnings("deprecation")
	public static int checkRowNull(XSSFRow xssfRow)
	{
		int num = 0;
		// 获取当前工作薄的每一列
		for (int j = xssfRow.getFirstCellNum(); j < xssfRow.getLastCellNum(); j++) {
			XSSFCell xssfcell = xssfRow.getCell(j);
			if (xssfcell == null || (("") == String.valueOf(xssfcell)) || xssfcell.toString().equals("")
					|| xssfcell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
				continue;
			} else {
				num++;
			}
		}
		return num;
	}

	/**
	 * 读表数据到列表，去除重复行的方法。
	 * 
	 * @param file
	 * @return List
	 */
	@SuppressWarnings("deprecation")
	public static ArrayList<String> readExcelData(File file)
	{
		ArrayList<String> Data_list = new ArrayList<String>();
		String TemplateData = null;
		String data = null;
		try {
			FileInputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFSheet sheet = wb.getSheetAt(0); // 获取第1个工作薄

			XSSFRow xssfrow0 = sheet.getRow(0);
			for (int j = xssfrow0.getFirstCellNum(); j < xssfrow0.getLastCellNum(); j++) {
				if (j == xssfrow0.getFirstCellNum()) {
					TemplateData = "null";
				} else {
					TemplateData += "\t" + "null";
				}
			}
			// 获取当前工作薄的每一行
			for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
				String TemplateArr[] = TemplateData.split("\t");
				XSSFRow xssfrow = sheet.getRow(i);

				// 获取当前工作薄的每一列
				for (int j = xssfrow.getFirstCellNum(); j < xssfrow.getLastCellNum(); j++) {
					XSSFCell xssfcell = xssfrow.getCell(j);
					if (xssfcell == null || (("") == String.valueOf(xssfcell)) || xssfcell.toString().equals("")
							|| xssfcell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
						continue;
					} else {
						String cellValue = String.valueOf(xssfcell);
						TemplateArr[j] = cellValue;
					}
				}
				for (int x = 0; x < TemplateArr.length; x++) {
					if (x == 0) {
						data = TemplateArr[x];
					} else {
						data += "\t" + TemplateArr[x];
					}
				}
				if (Data_list.contains(data)) {
					continue;
				} else {
					Data_list.add(data);
				}
			}
			is.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Data_list;
	}

	/**
	 * 写数据回指定Excel表文件。
	 * 
	 * @param file
	 */
	public static void rewriteExcelData(File file)
	{
		ArrayList<String> Data_list = readExcelData(file);
		createXlsx(file); // 新建同名文件覆盖原文件，达到清空数据效果
		try {
			FileInputStream is = new FileInputStream(file);
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheetAt(0); // 获取第1个工作薄
			// 写回数据
			for (int j = 0; j < Data_list.size(); j++) {
				XSSFRow row = sheet.createRow((short) j + 1);
				String str_row[] = Data_list.get(j).split("\t");
				for (int i = 0; i < str_row.length; i++) {
					// 在索引0的位置创建单元格（左上端）
					XSSFCell cell = row.createCell(i);
					if (str_row[i].equals("null")) {
						cell.setCellValue("");
					} else {
						cell.setCellValue(str_row[i]);
					}
				}
			}
			// 新建一输出文件流
			FileOutputStream fOut = new FileOutputStream(file);
			// 把相应的Excel 工作簿存盘
			workbook.write(fOut);
			fOut.flush();
			// 操作结束，关闭文件
			fOut.close();
			is.close();
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 写数据到指定xlsx格式文件
	 * 
	 * @param file
	 * @param logo
	 * @param data
	 * @param rownum
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void writeXlsx(File file, String logo, String data, int rownum) throws Exception
	{
		FileInputStream is = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(is);
		XSSFSheet sheet = wb.getSheetAt(0); // 获取第1个工作薄
		int cellIndex = 0;
		XSSFRow xssfrow = sheet.getRow(0);
		// 获取当前工作薄的每一列
		for (int j = xssfrow.getFirstCellNum(); j < xssfrow.getLastCellNum(); j++) {
			XSSFCell xssfcell = xssfrow.getCell(j);
			if (xssfcell != null) {
				String cellValue = String.valueOf(xssfcell).trim();
				if (cellValue.equals(logo)) {
					cellIndex = j;
				} else {
					continue;
				}
			}
		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int addrownum = rownum;
		// 指定行索引，创建一行数据, 行索引为当前最后一行的行索引 + 1
		int currentLastRowIndex = sheet.getLastRowNum();
		if (checkRowNull(sheet.getRow(currentLastRowIndex)) == 0) {
			addrownum = 0;
		}
		int newRowIndex = currentLastRowIndex + addrownum;
		XSSFRow newRow = null;
		if (addrownum == 0) {
			newRow = sheet.getRow(newRowIndex);
		} else {
			newRow = sheet.createRow(newRowIndex);
		}
		// 创建一个单元格，设置其内的数据格式为字符串，并填充内容，其余单元格类同
		XSSFCell newGenderCell = newRow.createCell(cellIndex, Cell.CELL_TYPE_STRING);
		newGenderCell.setCellValue(data);

		// 首先要创建一个原始Excel文件的输出流对象！
		FileOutputStream excelFileOutPutStream = new FileOutputStream(file);
		// 将最新的 Excel 文件写入到文件输出流中，更新文件信息！
		wb.write(excelFileOutPutStream);
		// 执行 flush 操作， 将缓存区内的信息更新到文件上
		excelFileOutPutStream.flush();
		// 使用后，及时关闭这个输出流对象， 好习惯，再强调一遍！
		excelFileOutPutStream.close();
		wb.close();
	}

	/**
	 * 在指定目录下提取Pre-lib name 的方法
	 * 
	 * @param input
	 * @return String
	 */
	public static String Extract_Pre_lib_name(String input)
	{
		String Pre_lib_name = null;
		String Pre_lib_name_Arr[] = input.split("-");
		if (Pre_lib_name_Arr.length == 5 && !input.contains("IRM")) {
			for (int i = 0; i < Pre_lib_name_Arr.length - 1; i++) {
				if (Pre_lib_name_Arr[0].contains("S")) {
					 if(i == 0) {
						 continue;
					 } else if (i == 1) {
						Pre_lib_name = Pre_lib_name_Arr[i];
					} else {
						Pre_lib_name += "-" + Pre_lib_name_Arr[i];
					}
				} else {
					if (i == 0) {
						Pre_lib_name = Pre_lib_name_Arr[i];
					} else {
						Pre_lib_name += "-" + Pre_lib_name_Arr[i];
					}
				}
			}
			String EndArr[] = Pre_lib_name_Arr[Pre_lib_name_Arr.length - 1].split("_");
			Pre_lib_name += "-" + EndArr[0];
		} else {
			for (int i = 0; i < Pre_lib_name_Arr.length - 1; i++) {
				if (Pre_lib_name_Arr[0].contains("S")) {
					 if(i == 0) {
						 continue;
					 } else if (i == 1) {
						Pre_lib_name = Pre_lib_name_Arr[i];
					} else {
						Pre_lib_name += "-" + Pre_lib_name_Arr[i];
					}
				} else {
					if (i == 0) {
						Pre_lib_name = Pre_lib_name_Arr[i];
					} else {
						Pre_lib_name += "-" + Pre_lib_name_Arr[i];
					}
				}
				
			}
			String EndArr[] = Pre_lib_name_Arr[Pre_lib_name_Arr.length-1].split("_");
			if (!EndArr[0].equals("IRM")) {
				Pre_lib_name += "-" + EndArr[0];
			}
		}
		return Pre_lib_name;
	}

	/**
	 * 把数据写成tsv格式文本的方法。
	 * 
	 * @param inputfile
	 * @param outputfile
	 */
	@SuppressWarnings("deprecation")
	public static void writeToTsv(String inputfile, String outputfile)
	{
		ArrayList<String> Data_list = new ArrayList<String>();
		String data = null;
		File file = new File(inputfile);
		// 读表数据
		try {
			FileInputStream is = new FileInputStream(file);
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFSheet sheet = wb.getSheetAt(0); // 获取第1个工作薄

			// 获取当前工作薄的每一行
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				XSSFRow xssfrow = sheet.getRow(i);
				int log = 0;
				// 获取当前工作薄的每一列
				for (int j = xssfrow.getFirstCellNum(); j < xssfrow.getLastCellNum(); j++) {
					XSSFCell xssfcell = xssfrow.getCell(j);
					if (xssfcell != null) {
						xssfcell.setCellType(Cell.CELL_TYPE_STRING); // 设置单元格类型为String类型，以便读取时候以string类型，也可其它
						String cellValue = xssfcell.getStringCellValue().trim();
						if (log == 0) {
							data = cellValue;
						} else {
							data += "\t" + cellValue;
						}
						log++;
					}
				}
				Data_list.add(data);
			}
			is.close();
			wb.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 写到输出文件
		try {
			FileWriter fw = new FileWriter(outputfile);
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < Data_list.size(); i++) {
				bw.write(Data_list.get(i) + "\r\n");
			}
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取文件的md5和文件绝对路径名
	 * @param file
	 * @return
	 */
	public static String md5sum(String file)
	{
		String cmd = "md5sum " + file;
		String data = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				data = line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 用SSh到10.2服务器上寻找文件的方法
	 * 
	 * @param command
	 * @throws Exception 
	 */
	public static String sshFun(String command) throws Exception
	{
		String data = null;
		String user = "zhirong_lu";
		String pass = "zhirong_lu";
		String host = "192.168.10.2";
		int port = 22;
			
		//String command = "mkdir " + PutPath;
		JSch jsch = new JSch();
		// 创建session并且打开连接，因为创建session之后要主动打开连接
		Session session = jsch.getSession(user, host, port);
		Hashtable<String, String> config = new Hashtable<String, String>();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setPassword(pass);
		session.connect();
		// 打开通道，设置通道类型，和执行的命令
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand(command);
		channelExec.setInputStream(null);
		BufferedReader input = new BufferedReader(new InputStreamReader(channelExec.getInputStream()));
		channelExec.connect();
		// 接收远程服务器执行命令的结果 
		String line = null;
		while ((line = input.readLine()) != null) {
			data = line;
		} // 循环读出系统调用返回值，保证脚本调用正常完成
		input.close(); 
		channelExec.disconnect();
		session.disconnect();

		Thread.sleep(1000);
		return data;
	}
	
	/**
	 * 調用ssh的方法，若ssh过程中拋出异常，程序自动修复，但ssh连续申请链接1000次都没有成功，程序直接退出执行！
	 * 
	 * @param filename
	 * @param PutPath
	 */
	public static String sequencingFileName(String command)
	{
		int x = 0;
		String data = null;
		while (true) {
			try {
				data = sshFun(command);
				if (data == null) {
					data = "NA";
				}
				if (x != 0) {
					System.out.println();
					System.out.println("ssh到10.2过程中拋出异常，但程序已自动修复成功！ ");
					x = 0;
				}
				break;
			} catch (Exception e) {
				//e.printStackTrace();
				x++;		
			}
			if (x == 100) {
				System.out.println();
				System.out.println("ssh到10.2连续申请链接100次都没有成功，程序直接退出执行！");
				return "off";
			} else {
				System.out.println();
				System.out.println("ssh到10.2过程中第" + x +"次拋出异常，但程序正在尝试自动修复！ ");
				continue;
			}		
		}
		return data;
	}

	/**
	 * 对每一列的数据计算并生产输出文件的方法。
	 * 
	 * @param Plasma_Excel
	 * @param Tissue_Excel
	 * @param Unknown_Excel
	 * @param Plasma_Tsv
	 * @param Tissue_Tsv
	 * @param Unknown_Tsv
	 * @param Input
	 */
	public static void calculatedData(String Plasma_Excel, String Tissue_Excel, String BC_Excel, String Test_Excel, String Plasma_Tsv,
			String Tissue_Tsv, String BC_Tsv, String Test_Tsv, String Input, String Por_name)
	{
		//System.out.println(Input);
		String data = null;
		File Plasma_File = new File(Plasma_Excel);
		File Tissue_File = new File(Tissue_Excel);
		File BC_File = new File(BC_Excel);
		File Test_File = new File(Test_Excel);
		HashMap<String, String> map_logo = new HashMap<String, String>(); // 数据结果的集合
		ArrayList<String> ID_data = searchFile(Input);
		ArrayList<String> Warning_List = new ArrayList<String>();
		ArrayList<String> Fail_List = new ArrayList<String>();
		String regEx = null;
		try {
			for (int i = 0; i < ID_data.size(); i++) {
				Warning_List.clear();
				Fail_List.clear();
				regEx = "[A-Z]{2}\\d{3}\\-\\d{3}";
				File file = null;
				String fileclass = null;
				int underlog = 0;
				String ID_dataArr[] = ID_data.get(i).split("\t");
				String Sample_ID = null;
				String Pre_lib_name = null;
				//System.out.println("+++: "+ ID_dataArr[0]);
				if (ID_dataArr[0].contains("DPM") || ID_dataArr[0].contains("DNA")) {
					Pre_lib_name = Extract_Pre_lib_name(ID_dataArr[0]);
					//System.out.println(ID_dataArr[0]+"===="+Pre_lib_name);
					String Pre_lib_name_Arr[] = Pre_lib_name.split("-");
					if (Pre_lib_name_Arr.length < 3) {
						if (ID_dataArr[0].equals("S01-DPM-LC002-110_S1_R1_001")) {
							Pre_lib_name = ID_dataArr[0];
					    	file = Plasma_File;
					    } else {
							file = Test_File;
							Pre_lib_name = ID_dataArr[0];
							underlog = 1;
							fileclass = "test";
					    }
					}
				} else {
					Pre_lib_name = ID_dataArr[0];
					file = Test_File;
					underlog = 1;
					fileclass = "test";
				}
				if (Pre_lib_name != null) {
					if ((Pre_lib_name.contains("-BD") || Pre_lib_name.contains("-PS")) && file == null) {
						file = Plasma_File;
					} else if (Pre_lib_name.contains("-F") && file == null) {
						file = Tissue_File;
					} else if (Pre_lib_name.contains("-BC") && file == null) {
						file = BC_File;
					} else if (file == null) {
						file = Test_File;
						fileclass = "test";
					}
					Sample_ID = Regular_Expression(Pre_lib_name, regEx);
					if (Sample_ID == null) {
						if (underlog == 0) {
							Sample_ID = Extract_Sample_ID(Pre_lib_name);
						}
					}
				} else {
					continue;
				}
				String Sequencing_Info = Extract_Sequencing_Info(ID_dataArr[1]);

				map_logo.put("Sample ID", Sample_ID); // 向数据结果集合添加Sample ID的值
				map_logo.put("Pre-lib name", Pre_lib_name); // 向数据结果集合添加Pre-lib name的值
				map_logo.put("Identification name", ID_dataArr[0]); // 向数据结果集合添加Identification name的值
				map_logo.put("Sequencing info", Sequencing_Info); // 向数据结果集合添加Sequencing info的值

				String cmd = "find /Src_Data2/nextseq500 /Src_Data2/x10/ -type f -name " + ID_dataArr[0] + ".fastq.gz";
				Process process = Runtime.getRuntime().exec(cmd);
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String Sequencing_file_name = null;
				if ((Sequencing_file_name = input.readLine()) != null) {
					map_logo.put("Sequencing file name", Sequencing_file_name); // 向数据结果集合添加Sequencing file name的值
				} else {
					String command = "find /iron/nextseq500/outputdata/"+ Sequencing_Info +"/Data/Intensities/BaseCalls/ -type f -name " + ID_dataArr[0] + ".fastq.gz";
					Sequencing_file_name = sequencingFileName(command);
					if (Sequencing_file_name.equals("off")) {
						map_logo.put("Sequencing file name", "10.5上无该文件，而ssh10.2过程中出异常！");
					} else {
						map_logo.put("Sequencing file name", Sequencing_file_name);
					}
				}

				String deduped_cvg = null;
				String deduped_hsmetrics = null;
				String origin_hsmetrics = null;
				String bisulfite = null;
				String QC_result = null;
				String deduped_bam = null;
				String deduped_insertSize = null;
				int tag = 0;

				String InputArr[] = Input.split("/");
				String cmd1 = null;
				if (InputArr.length < 3) {
					cmd1 = "find " + Input + "/" + Sequencing_Info + "/" + Por_name + " -name " + ID_dataArr[0] + "*";
				} else {
					cmd1 = "find " + Input + " -name " + ID_dataArr[0] + "*";
				}
				Process process1 = Runtime.getRuntime().exec(cmd1);
				BufferedReader input1 = new BufferedReader(new InputStreamReader(process1.getInputStream()));
				String line1 = null;
				while ((line1 = input1.readLine()) != null) {
					if (line1.endsWith("sorted.deduplicated.bam.perTarget.coverage")) {
						deduped_cvg = line1;
						map_logo.put("Pre-lib name*sorted.deduplicated.bam.perTarget.coverage(Uniformity (0.2X mean))", md5sum(deduped_cvg));
						tag++;
					} else if (line1.endsWith("sorted.deduplicated.bam.hsmetrics.txt")) {
						deduped_hsmetrics = line1;
						map_logo.put("Pre-lib name*sorted.deduplicated.bam.hsmetrics.txt(Deduped mean bait coverage; Deduped mean target coverage; % target bases > 30X)", md5sum(deduped_hsmetrics));
						tag++;
					} else if (line1.endsWith("sorted.deduplicated.bam.insertSize.txt")) {
						deduped_insertSize = line1;
						map_logo.put("Pre-lib name*sorted.deduplicated.bam.insertSize.txt(Mean_insert_size; Median_insert_size)", md5sum(deduped_insertSize));
						tag++;
					} else if (line1.endsWith("sorted.bam.hsmetrics.txt")) {
						origin_hsmetrics = line1;
						map_logo.put("Pre-lib name*sorted.bam.hsmetrics.txt(Total PF reads; On target%; Pre-dedup mean bait coverage; Bait set)", md5sum(origin_hsmetrics));
						tag++;
					} else if (line1.endsWith("PE_report.txt")) {
						bisulfite = line1;
						map_logo.put("Pre-lib name*PE_report.txt(C methylated in CHG context; C methylated in CHH context; C methylated in CpG context)", md5sum(bisulfite));
						tag++;
					} else if (line1.endsWith("hsmetrics.QC.xls") || line1.endsWith("hsmetrics.QC.xlsx")) {
						QC_result = line1;
						map_logo.put("Pre-lib name*hsmetrics.QC.xls*(QC result; Date of QC; Date of path update)", md5sum(QC_result));
						tag++;
					} else if (line1.endsWith("sorted.deduplicated.bam")) {
						deduped_bam = line1;
						tag++;
					} else {
						continue;
					}
				}
				if (deduped_cvg == null) {
					map_logo.put("Pre-lib name*sorted.deduplicated.bam.perTarget.coverage(Uniformity (0.2X mean))", "NA");
				}
				if (deduped_hsmetrics == null) {
					map_logo.put("Pre-lib name*sorted.deduplicated.bam.hsmetrics.txt(Deduped mean bait coverage; Deduped mean target coverage; % target bases > 30X)", "NA");
				}
				if (deduped_insertSize == null) {
					map_logo.put("Pre-lib name*sorted.deduplicated.bam.insertSize.txt(Mean_insert_size; Median_insert_size)", "NA");
				}
				if (origin_hsmetrics == null) {
					map_logo.put("Pre-lib name*sorted.bam.hsmetrics.txt(Total PF reads; On target%; Pre-dedup mean bait coverage; Bait set)", "NA");
				}
				if (bisulfite == null) {
					map_logo.put("Pre-lib name*PE_report.txt(C methylated in CHG context; C methylated in CHH context; C methylated in CpG context)", "NA");
				}
				if (QC_result == null) {
					map_logo.put("Pre-lib name*hsmetrics.QC.xls*(QC result; Date of QC; Date of path update)", "NA");
				}			
				if (tag == 0) {
					continue;
				}
				
				String Map = null;
				String PF = null;
				String OnTarget = null;
				String BaitCvg = null;
				String DedupBaitCvg = null;
				String DedupCvg = null;
				String Target30X = null;
				String Uniformity = null;
				String CHG = null;
				String CHH = null;

				// 向数据结果集合添加Mapping%的值
				/*data = null;
				String Start = ID_dataArr[0];
				String End = "sorted.bam";
				String flagstat = null;
				int tar = 0;
				ArrayList<String> filelist = new ArrayList<String>();
				filelist.clear();
				ArrayList<String> data4 = Find_flagstat_xls(Input, Start, End, filelist);
				if (filelist.size() == 0) {
					map_logo.put("flagstat.xls(Mapping%)", "NA");
				} else {
					if (filelist.size() == 1) {
						flagstat = md5sum(filelist.get(0));
					} else {
						for (int x = 0; x < filelist.size(); x++) {
							if (x == 0) {
								flagstat = md5sum(filelist.get(x));
								continue;
							}
							flagstat += " || " + md5sum(filelist.get(x));
						}
					}
					map_logo.put("flagstat.xls(Mapping%)", flagstat);
				}
				for (int x = 0; x < data4.size(); x++) {
					if (x == 0) {
						if (!(data4.get(x).equals("NA"))) {
							if ((data4.get(x).equals("%"))){
								//System.out.println("data4.get(x) = " + data4.get(x) + "  " + x);
								continue;
							} else {
								data = data4.get(x);
								tar++;
							}
						}
					} else if (data != null) {
						if (!(data4.get(x).equals("NA"))) {
							if ((data4.get(x).equals("%"))){
								//System.out.println("data4.get(x) = " + data4.get(x) + "  " + x + "data = " +data);
								continue;
							} else {
								data += "__" + data4.get(x);
								tar++;
							}
						}
					} else {
						if (!(data4.get(x).equals("NA"))) {
							if ((data4.get(x).equals("%"))){
								//System.out.println("data4.get(x) = " + data4.get(x) + "  " + x);
								continue;
							} else {
								data = data4.get(x);
								tar++;
							}
						}
					}
				}
				if (tar != 0) {
					map_logo.put("Mapping%", data);
				} else {
					map_logo.put("Mapping%", "NA");
				}
				if (tar == 1) {
					Map = data;
				} else {
					Map = "NA";
				}*/
				
				// 向数据结果集合添加Mapping%的值
				data = null;
				if (bisulfite != null) {
					String[] cmd4 = { "awk", "/Mapping efficiency/ {print $3}", bisulfite };
					data = Linux_Cmd(cmd4);
					if (data == null) {
						data = "NA";
					}
					map_logo.put("Mapping%", data);
					map_logo.put("PE report.txt(Mapping%)", md5sum(bisulfite));
					Map = data;
				} else {
					map_logo.put("Mapping%", "NA");
					map_logo.put("PE report.txt(Mapping%)", "NA");
					Map = "NA";
				}

				// 向数据结果集合添加Total PF reads的值
				data = null;
				if (origin_hsmetrics != null) {
					String[] cmd5 = { "awk", "NR==8 {print $7 }", origin_hsmetrics };
					data = Linux_Cmd(cmd5);
					map_logo.put("Total PF reads", data);
					PF = data;
				} else {
					PF = "NA";
					map_logo.put("Total PF reads", "NA");
				}

				// 向数据结果集合添加Mean_insert_size的值
				data = null;
				if (deduped_insertSize != null) {
					String[] cmd6 = { "awk", "-F", "\t", "NR==8 {print $5}", deduped_insertSize };
					data = Linux_Cmd(cmd6);
					map_logo.put("Mean_insert_size", data);
				} else {
					map_logo.put("Mean_insert_size", "NA");
				}

				// 向数据结果集合添加Median_insert_size的值
				data = null;
				if (deduped_insertSize != null) {
					String[] cmd7 = { "awk", "-F", "\t", "NR==8 {print $1}", deduped_insertSize };
					data = Linux_Cmd(cmd7);
					map_logo.put("Median_insert_size", data);
				} else {
					map_logo.put("Median_insert_size", "NA");
				}

				// 向数据结果集合添加On target%的值
				data = null;
				if (origin_hsmetrics != null) {
					String[] cmd8 = { "awk", "NR==8 {print $19}", origin_hsmetrics };
					data = Linux_Cmd(cmd8);
					map_logo.put("On target%", data);
					OnTarget = data;
				} else {
					OnTarget = "NA";
					map_logo.put("On target%", "NA");
				}

				// 向数据结果集合添加Pre-dedup mean bait coverage的值
				data = null;
				if (origin_hsmetrics != null) {
					String[] cmd9 = { "awk", "NR==8 {print $22}", origin_hsmetrics };
					data = Linux_Cmd(cmd9);
					map_logo.put("Pre-dedup mean bait coverage", data);
					BaitCvg = data;
				} else {
					BaitCvg = "NA";
					map_logo.put("Pre-dedup mean bait coverage", "NA");
				}

				// 向数据结果集合添加Deduped mean bait coverage的值
				data = null;
				if (deduped_hsmetrics != null) {
					String[] cmd10 = { "awk", "NR==8 {print $22 }", deduped_hsmetrics };
					data = Linux_Cmd(cmd10);
					map_logo.put("Deduped mean bait coverage", data);
					DedupBaitCvg = data;
				} else {
					DedupBaitCvg = "NA";
					map_logo.put("Deduped mean bait coverage", "NA");
				}

				// 向数据结果集合添加Deduped mean target coverage的值
				data = null;
				if (deduped_hsmetrics != null) {
					String[] cmd11 = { "awk", "NR==8 {print $23 }", deduped_hsmetrics };
					data = Linux_Cmd(cmd11);
					map_logo.put("Deduped mean target coverage", data);
					DedupCvg = data;
				} else {
					DedupCvg = "NA";
					map_logo.put("Deduped mean target coverage", "NA");
				}

				// 向数据结果集合添加% target bases > 30X的值
				data = null;
				if (deduped_hsmetrics != null) {
					String[] cmd12 = { "awk", "NR==8 {print $39 }", deduped_hsmetrics };
					data = Linux_Cmd(cmd12);
					map_logo.put("% target bases > 30X", data);
					Target30X = data;
				} else {
					Target30X = "NA";
					map_logo.put("% target bases > 30X", "NA");
				}

				// 向数据结果集合添加Uniformity (0.2X mean)的值
				data = null;
				if (QC_result != null) {
					String[] cmd13 = { "awk", "-F", "\t", "/UNIFORMITY/ {print $4}", QC_result };
					Process process13 = Runtime.getRuntime().exec(cmd13);
					BufferedReader input13 = new BufferedReader(new InputStreamReader(process13.getInputStream()));
					String line = null;
					while ((line = input13.readLine()) != null) {
						if (data == null) {
							data = line;
						} else {
							data += "\t" + line;
						}
					}
					if (data == null) {
						if (deduped_cvg != null) {
							String[] cmd1_3 = { "/anchordx-opt/local/bin/Rscript",
									"/home/jiacheng_chuan/Ironman/DataArrangement/calcUniformity.R", deduped_cvg };
							Process process1_3 = Runtime.getRuntime().exec(cmd1_3);
							BufferedReader input1_3 = new BufferedReader(
									new InputStreamReader(process1_3.getInputStream()));
							String line1_3 = null;
							data = null;
							int log13 = 0;
							while ((line1_3 = input1_3.readLine()) != null) {
								if (log13 == 1) {
									String data13[] = line1_3.split("\t");
									data = data13[1];
									map_logo.put("Uniformity (0.2X mean)", data);
									Uniformity = data;
									break;
								} else {
									data += "\t" + line1_3;
								}
								log13++;
							}
						} else {
							map_logo.put("Uniformity (0.2X mean)", "NA");
							Uniformity = "NA";
						}
					} else {
						map_logo.put("Uniformity (0.2X mean)", data);
						Uniformity = data;
					}
				} else {
					if (deduped_cvg != null) {
						String[] cmd1_3 = { "/anchordx-opt/local/bin/Rscript", "/home/jiacheng_chuan/Ironman/DataArrangement/calcUniformity.R",
								deduped_cvg };
						Process process1_3 = Runtime.getRuntime().exec(cmd1_3);
						BufferedReader input1_3 = new BufferedReader(
								new InputStreamReader(process1_3.getInputStream()));
						String line1_3 = null;
						data = null;
						int log13 = 0;
						while ((line1_3 = input1_3.readLine()) != null) {
							if (log13 == 1) {
								String data13[] = line1_3.split("\t");
								data = data13[1];
								map_logo.put("Uniformity (0.2X mean)", data);
								Uniformity = data;
								break;
							} else {
								data += "\t" + line1_3;
							}
							log13++;
						}
					} else {
						map_logo.put("Uniformity (0.2X mean)", "NA");
						Uniformity = "NA";
					}
				}

				// 向数据结果集合添加C methylated in CHG context的值
				data = null;
				if (bisulfite != null) {
					String[] cmd14 = { "awk", "/C methylated in CHG context/ {print $6}", bisulfite };
					data = Linux_Cmd(cmd14);
					if (data == null) {
						data = "NA";
					}
					map_logo.put("C methylated in CHG context", data);
					CHG = data;
				} else {
					map_logo.put("C methylated in CHG context", "NA");
					CHG = "NA";
				}

				// 向数据结果集合添加C methylated in CHH context的值
				data = null;
				if (bisulfite != null) {
					String[] cmd15 = { "awk", "/C methylated in CHH context/ {print $6}", bisulfite };
					data = Linux_Cmd(cmd15);
					if (data == null) {
						data = "NA";
					}
					map_logo.put("C methylated in CHH context", data);
					CHH = data;
				} else {
					map_logo.put("C methylated in CHH context", "NA");
					CHH = "NA";
				}

				// 向数据结果集合添加C methylated in CpG context的值
				data = null;
				if (bisulfite != null) {
					String[] cmd15 = { "awk", "/C methylated in CpG context/ {print $6}", bisulfite };
					data = Linux_Cmd(cmd15);
					map_logo.put("C methylated in CpG context", data);
				} else {
					map_logo.put("C methylated in CpG context", "NA");
				}

				// 向数据结果集合添加QC result的值
				data = null;
				if (QC_result != null) {
					data = QC_result;
					map_logo.put("QC result", data);
				} else {
					map_logo.put("QC result", "NA");
				}

				// 向数据结果集合添加Date of QC的值
				data = null;
				if (deduped_bam != null) {
					data = getModifiedTime(deduped_bam);
					map_logo.put("Date of QC", data);
				} else {
					map_logo.put("Date of QC", "NA");
				}

				// 向数据结果集合添加Path to sorted.deduped.bam的值
				data = null;
				if (deduped_bam != null) {
					data = deduped_bam;
					map_logo.put("Path to sorted.deduped.bam", data);
					
					//根据bam的路径确实是否为test样本
					if (fileclass == null) {
						String str[] = data.split("/");
						String str_Sample[] = Sample_ID.split("-");
						if (str.length == 8) {
							if (!str[5].equals("analysis") && !str[5].equals("methylation") && !str[5].contains(str_Sample[0])) {
								file = Test_File;
							}							
						}
					}
					
				} else {
					map_logo.put("Path to sorted.deduped.bam", "NA");
				}

				// 向数据结果集合添加Date of path update的值
				data = null;
				if (deduped_bam != null) {
					data = getModifiedTime(deduped_bam);
					map_logo.put("Date of path update", data);
				} else {
					map_logo.put("Date of path update", "NA");
				}

				// 向数据结果集合添加Bait set的值
				data = null;
				if (origin_hsmetrics != null) {
					String[] cmd20 = { "awk", "NR==8 {print $1 }", origin_hsmetrics };
					data = Linux_Cmd(cmd20);
					map_logo.put("Bait set", data);
				} else {
					map_logo.put("Bait set", "NA");
				}

				// 向数据结果集合添加log2(CPM+1)的值
				data = null;				
				//String InputArr[] = Input.split("/");
				String cmd23 = null;
				if (InputArr.length < 3) {
					cmd23 = "find " + Input + "/" + Sequencing_Info + "/" + Por_name + " -name " + ID_dataArr[0] + "*.WM.*.stat";
				} else {
					cmd23 = "find " + Input + " -name " + ID_dataArr[0] + "*.WM.*.stat";
				}				
				//String cmd23 = "find " + Input + " -name " + ID_dataArr[0] + "*.WM.*.stat";
				Process process23 = Runtime.getRuntime().exec(cmd23);
				BufferedReader input23 = new BufferedReader(new InputStreamReader(process23.getInputStream()));
				String line23 = null;
				while ((line23 = input23.readLine()) != null) {
					if (data == null) {
						data = line23;
					} else {
						data += " " + line23;
					}
				}
				if (data == null) {
					map_logo.put("log2(CPM+1)", "NA");
				} else {
					map_logo.put("log2(CPM+1)", data);
				}

				// 向数据结果集合添加Sample QC的值
				String Result = null;
				// Map
				if (!(Map.equals("NA"))) {
					String NumStrArr[] = Map.split("%");
					double num = Double.valueOf(NumStrArr[0]);
					if (num > 50) {
						Result = "Pass";
					} else {
						Result = "Warning";
						Warning_List.add("Map");
					}
				}
				// PF
				if (!(PF.equals("NA"))) {
					int num = Integer.valueOf(PF);
					if (!(num > 10)) {
						Result = "Warning";
						Warning_List.add("PF");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// OnTarget
				if (!(OnTarget.equals("NA"))) {
					double num = Double.valueOf(OnTarget);
					if (!(num > 0.4)) {
						Result = "Warning";
						Warning_List.add("OnTarget");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// BaitCvg
				if (!(BaitCvg.equals("NA"))) {
					double num = Double.valueOf(BaitCvg);
					if (!(num > 500)) {
						Result = "Warning";
						Warning_List.add("BaitCvg");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// DedupBaitCvg
				if (!(DedupBaitCvg.equals("NA"))) {
					double num = Double.valueOf(DedupBaitCvg);
					if (!(num > 100)) {
						Result = "Warning";
						Warning_List.add("DedupBaitCvg");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// DedupCvg
				if (!(DedupCvg.equals("NA"))) {
					double num = Double.valueOf(DedupCvg);
					if (!(num > 50)) {
						Result = "Fail";
						Fail_List.add("DedupCvg");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// Target30X
				if (!(Target30X.equals("NA"))) {
					double num = Double.valueOf(Target30X);
					if (!(num > 0.5)) {
						Result = "Fail";
						Fail_List.add("Target30X");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// Uniformity
				if (!(Uniformity.equals("NA"))) {
					double num = Double.valueOf(Uniformity);
					if (!(num > 0.85)) {
						if (Result == null) {
							Result = "Warning";
						} else if (!(Result.equals("Fail"))) {
							Result = "Warning";
						}
						Warning_List.add("Uniformity");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// CHG
				if (!(CHG.equals("NA"))) {
					String NumStrArr[] = CHG.split("%");
					double num = Double.valueOf(NumStrArr[0]);
					if (num >= 3) {
						Result = "Fail";
						Fail_List.add("CHG");
					} else if (num < 3 && num >= 1) {
						if (Result == null) {
							Result = "Warning";
						} else if (!(Result.equals("Fail"))) {
							Result = "Warning";
						}
						Warning_List.add("CHG");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// CHH
				if (!(CHH.equals("NA"))) {
					String NumStrArr[] = CHH.split("%");
					double num = Double.valueOf(NumStrArr[0]);
					if (num >= 3) {
						Result = "Fail";
						Fail_List.add("CHH");
					} else if (num < 3 && num >= 1) {
						if (Result == null) {
							Result = "Warning";
						} else if (!(Result.equals("Fail"))) {
							Result = "Warning";
						}
						Warning_List.add("CHH");
					} else {
						if (Result == null) {
							Result = "Pass";
						}
					}
				}
				// last
				if (Result == null) {
					Result = "NA";
				}
				map_logo.put("Sample QC", Result);

				// 向数据结果集合添加Failed QC Detail的值
				data = null;
				if (Fail_List.size() != 0) {
					for (int t = 0; t < Fail_List.size(); t++) {
						if (t == 0) {
							data = Fail_List.get(t);
						} else {
							data += ";" + Fail_List.get(t);
						}
					}
					map_logo.put("Failed QC Detail", data);
				} else {
					map_logo.put("Failed QC Detail", "NA");
				}

				// 向数据结果集合添加Warning QC Detail的值
				data = null;
				if (Warning_List.size() != 0) {
					for (int t = 0; t < Warning_List.size(); t++) {
						if (t == 0) {
							data = Warning_List.get(t);
						} else {
							data += ";" + Warning_List.get(t);
						}
					}
					map_logo.put("Warning QC Detail", data);
				} else {
					map_logo.put("Warning QC Detail", "NA");
				}

				int rownum = 1;
				for (String key : map_logo.keySet()) {
					writeXlsx(file, key, map_logo.get(key), rownum); // 写数据到Excel表文件
					rownum = 0;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 血浆表
		while (removeNullRow(Plasma_File) != 0) {
			removeNullRow(Plasma_File); // 去除空行
		}
		rewriteExcelData(Plasma_File); // 去除重复行
		writeToTsv(Plasma_Excel, Plasma_Tsv); // 写成tsv格式文件

		// 组织表
		while (removeNullRow(Tissue_File) != 0) {
			removeNullRow(Tissue_File); // 去除空行
		}
		rewriteExcelData(Tissue_File); // 去除重复行
		writeToTsv(Tissue_Excel, Tissue_Tsv); // 写成tsv格式文件
		
		// 白细胞表
		while (removeNullRow(BC_File) != 0) {
			removeNullRow(BC_File); // 去除空行
		}
		rewriteExcelData(BC_File); // 去除重复行
		writeToTsv(BC_Excel, BC_Tsv); // 写成tsv格式文件

		// 测试数据表
		while (removeNullRow(Test_File) != 0) {
			removeNullRow(Test_File); // 去除空行
		}
		rewriteExcelData(Test_File); // 去除重复行
		writeToTsv(Test_Excel, Test_Tsv); // 写成tsv格式文件
	}
}
