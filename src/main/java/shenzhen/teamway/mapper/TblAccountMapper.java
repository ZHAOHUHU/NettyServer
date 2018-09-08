package shenzhen.teamway.mapper;

import java.util.List;
import shenzhen.teamway.pojo.TblAccount;

public interface TblAccountMapper {
    int deleteByPrimaryKey(Integer userid);

    int insert(TblAccount record);

    TblAccount selectByPrimaryKey(Integer userid);

    List<TblAccount> selectAll();

    int updateByPrimaryKey(TblAccount record);
}