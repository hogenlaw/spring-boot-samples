package tk.mybatis.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 自己的 mapper
 * 特别注意，该接口不能被扫描，否则会报错
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
