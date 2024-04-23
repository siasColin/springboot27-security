package cn.net.ssd.mapper.sysManage;

import java.util.List;
import java.util.Map;

public interface SensitiveMapper {
	/**
	 * 添加敏感词汇信息
	 */
	public Integer add(Map<String, Object> map) throws Exception;
	
	 /**
	  * 
	  *
	  * @Title: findSensitiveAll 
	  * @Description: 查询所有敏感词
	  * @param @param map
	  * @param @return
	  * @param @throws Exception    设定文件 
	  * @return JSONObject    返回类型 
	  * @throws
	  */
	 public List<String> findSensitiveAll(Map<String, Object> map) throws Exception;

	/**
	 * 查询敏感词汇信息
	 */
	public Integer findCount(Map<String, Object> map) throws Exception;

	/**
	 * 查询敏感词汇信息
	 */
	public List<Map<String,Object>> find(Map<String, Object> map) throws Exception;

	/**
	 * 查询每个敏感词汇详细信息
	 * 
	 */
	public Object findDetialById(Map<String, Object> map) throws Exception;
	
	/**
	 *
	 * @Title: findUnionWordDetial 
	 * @Description: 查询关键词是否存在
	 * @param @param map
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return Integer    返回类型 
	 * @throws
	 */
	public Integer findUnionWordDetial(Map<String, Object> map) throws Exception;

	/**
	 * 修改敏感词汇信息
	 */
	public Integer update(Map<String, Object> map) throws Exception;

	/**
	 * 删除敏感词汇信息
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer delete(Map<String, Object> map) throws Exception;
	
	/**
	 * 
	 *
	 * @Title: findSensitiveExport 
	 * @Description:查询所有敏感字词 导出 
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @return List<Sensitive>    返回类型 
	 * @throws
	 */
	public List<Map<String,Object>> findSensitiveExport(Map<String, Object> map) throws Exception;

	public List<Map<String,Object>> findAllOrg(Map<String, Object> orgMap) throws Exception;
}
