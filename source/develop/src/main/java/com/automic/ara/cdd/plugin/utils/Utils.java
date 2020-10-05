package com.automic.ara.cdd.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import com.automic.ara.cdd.plugin.backend.rest.models.DetailModel;

public class Utils {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.equals("");
	}
	
	/**
	 * To fetch the list of unarchieved data
	 * @param dataList
	 * @return
	 */
	public static List<? extends DetailModel> unarchievedData(List<? extends DetailModel> dataList){
		List<DetailModel> result = new ArrayList<>();
		if(dataList.isEmpty())
			return result;
		for(DetailModel data : dataList){
			if(!data.isArchived()){
				result.add(data);
			}
		}
		return result;
	}

	/**
	 * Convert numeric number to hexadecimal string to contain at least 8 characters
	 * @param id
	 * @return hexadecimal encoded string
	 */
	public static String decimalToHex(long id) {
		return String.format("%08X", id);
	}
}
