package com.aiitec.technicalreserve.wheelview;

import java.io.Serializable;


/**
 * regionInfo 的 region对象
 * @author Anthony
 *
 */
@SuppressWarnings("serial")
public class Region implements Serializable{
	
	/**父id*/
	private int parentId = -1;
	private int id = -1;
	/**拼音*/
	private String pinyin;
	private String name;
	/**
	 * 获取父Id
	 * @return 父id
	 */
	public int getParentId() {
		return parentId;
	}
	/**
	 * 设置父id
	 * @param parent_id 父id
	 */
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * 获取拼音
	 * @return 拼音
	 */
	public String getPinyin() {
		return pinyin;
	}
	/**
	 * 设置拼音
	 * @param pinyin 拼音
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
