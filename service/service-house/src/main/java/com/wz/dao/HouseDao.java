package com.wz.dao;

import com.wz.entity.House;
import com.wz.vo.HouseQueryVo;
import com.wz.vo.HouseVo;
import com.github.pagehelper.Page;

public interface HouseDao extends BaseDao<House> {
    Page<HouseVo> findPageList(HouseQueryVo houseQueryVo);
}
