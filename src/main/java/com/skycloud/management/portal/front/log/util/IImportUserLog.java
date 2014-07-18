package com.skycloud.management.portal.front.log.util;

import java.io.IOException;
import java.util.List;

import com.skycloud.management.portal.front.log.entity.TUserLogVO;

public interface IImportUserLog {

	boolean importLog(List<TUserLogVO> list, int log_file_size) throws IOException;
}
