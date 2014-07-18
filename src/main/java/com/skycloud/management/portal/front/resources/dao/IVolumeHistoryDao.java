package com.skycloud.management.portal.front.resources.dao;

import com.skycloud.management.portal.front.resources.dao.bo.VolumeHistoryBO;

public interface IVolumeHistoryDao {

  
  
  void insertVolumeHistory(VolumeHistoryBO volumeHistory) throws Exception;
  
  void updateVolumeHistoryState(VolumeHistoryBO volumeHistory) throws Exception;
  
  void deleteVolumeHistory(VolumeHistoryBO volumeHistory) throws Exception;
}
