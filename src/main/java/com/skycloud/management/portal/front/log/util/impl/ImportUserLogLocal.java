package com.skycloud.management.portal.front.log.util.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.util.IImportUserLog;

/**
 * 手工导出日志到本地文件
 * 
 * @author lh 2012.4.6
 */
public class ImportUserLogLocal implements IImportUserLog {

	/**
	 * @param list
	 *            数据结果集
	 * @param log_file_size
	 *            日志文件大小
	 */
	@Override
	public boolean importLog(List<TUserLogVO> list, int log_file_size) throws IOException {
		boolean returnflag = true;
		// 读取配置文件
		if (list == null || list.isEmpty()) {
			return false;
		}
		FileOutputStream fos = null;
		PrintWriter w = null;
		try {
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/config.properties");
			Properties p = new Properties();
			p.load(stream);
			String filepath = p.getProperty("logPath");
			File dir = new File(filepath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			long maxLength = log_file_size * 1024 * 1024;
			Date date = new Date();
			// String filename=String.format("%tF", date);
			// 日期格式的文件名精确到时分秒
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String filename = sdf.format(date);
			File file = new File(filepath + filename + ".log");
			if (!file.exists()) {
				file.createNewFile();
			}
			fos = new FileOutputStream(file);
			w = new PrintWriter(new OutputStreamWriter(fos));
			StringBuffer sb;
			int count = 1; // 多批日志后缀标识
			sb = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				TUserLogVO to = list.get(i);
				sb.append(to.getId());
				sb.append("|");
				sb.append(to.getRoleId());
				sb.append("|");
				sb.append(to.getMethodName());
				sb.append("|");
				sb.append(to.getClassName());
				sb.append("|");
				sb.append(to.getParameters());
				sb.append("|");
				sb.append(to.getFunctionName());
				sb.append("|");
				sb.append(to.getModuleName());
				sb.append("|");
				sb.append(to.getIp());
				sb.append("|");
				sb.append(to.getType());
				sb.append("|");
				sb.append(to.getComment());
				sb.append("|");
				sb.append(to.getCreateDt());
				sb.append("|");
				sb.append(to.getMemo());
				sb.append("|");
				sb.append(to.getAccount());
				sb.append("\r\n");
				long n = sb.toString().getBytes().length;
				// 判断写入文件大小是否大于设置的最大限度，如果大于限定值则再新写一个文件。
				if (file.length() + n > maxLength) {
					fos.close();
					w.close();
					file = new File(filepath + filename + "_" + count + ".log");
					fos = new FileOutputStream(file);
					w = new PrintWriter(new OutputStreamWriter(fos));
					count++;
				}
				// 20K大约100条或者已经到列表对象的末尾,则写入文件
				if (n > 20480 || i == list.size() - 1) {
					w.print(sb.toString());
					w.flush();
					sb = null;
					sb = new StringBuffer();
					// 将写入成功的数据清除
				}
			}
		}
		catch (Exception e) {
			returnflag = false;
			e.printStackTrace();
			throw new IOException("手工写日志文件失败。失败原因：" + e.getMessage());
		}
		finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (w != null) {
					w.close();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				returnflag = false;
			}
		}
		return returnflag;
	}

}
