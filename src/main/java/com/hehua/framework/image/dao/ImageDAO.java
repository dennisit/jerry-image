/**
 * 
 */
package com.hehua.framework.image.dao;

import java.util.Collection;
import java.util.List;

import javax.inject.Named;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.hehua.framework.image.domain.Image;

/**
 * @author zhihua
 *
 */
@Named
public interface ImageDAO {

    @Insert("insert into `image` (`fid`,`width`,`height`,`size`,`format`,`status`,`create_time`,`bucket`) values (#{fid},#{width},#{height},#{size},#{format},#{status},#{createTime},#{bucket})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public long insert(Image image);

    @Select("select `id`,`fid`,`width`,`height`,`size`,`format`,`status`,`create_time`,`bucket` from `image` where `id` = #{id}")
    public Image getById(@Param("id") long id);

    @Select("select `id`,`fid`,`width`,`height`,`size`,`format`,`status`,`create_time`,`bucket` from `image` where `fid` = #{fid}")
    public Image getByFid(@Param("fid") String fid);

    public List<Image> getByIds(@Param("ids") Collection<Long> ids);

    public List<Image> getByFids(@Param("ids") Collection<String> ids);

}
