/**
 * 2011-11-29  下午05:00:22  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.skycloud.management.portal.front.command.res.RebootVirtualMachine;
import com.skycloud.management.portal.front.command.res.StartVirtualMachine;
import com.skycloud.management.portal.front.command.res.StopVirtualMachine;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

/**
 * @author shixq
 * @version $Revision$ 下午05:00:22
 */
public class ResourcesUtil {
  public static String parameter;
  public static String operation;

  public static void returnAsyncJobCommand(ResourcesModifyVO vmModifyVO) {
    BaseCommandPo commandType;
    if (vmModifyVO.getCommandType() instanceof StartVirtualMachine) {
      commandType = new StartVirtualMachine();// 开机
    } else if (vmModifyVO.getCommandType() instanceof StopVirtualMachine) {
      commandType = new StopVirtualMachine();// 关机
    } else if (vmModifyVO.getCommandType() instanceof RebootVirtualMachine) {
      commandType = new RebootVirtualMachine();// 重启
    } else {
      return;
    }
    commandType.getCOMMAND();
  }

  public static String resultTOString(int resultNum) {
    if (resultNum == 0) {
      return "操作失败，请重新操作！";
    }else {
      return "操作成功!";
    }
  }
  
  /**
	 * 获取创建时的name,由当前时间和四位随机数组成
	 * @return
	 */
	public static String getresName(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sf.format(new Date(System.currentTimeMillis()));
		double random = Math.random()*9000+1000;
		String name = date.concat(String.valueOf((int)Math.rint(random)));
		return name;
	}
}
