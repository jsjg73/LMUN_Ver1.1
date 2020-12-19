package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteM;
import com.mycompany.myapp.model.RouteS;

@Repository
public class MapDAOImpl implements MapDAO{
	@Autowired
	private SqlSession sqlSession;

	
	@Override
	public RouteM routeSearch(String id) {
		return sqlSession.selectOne("mapns.routeSearch", id);
	}
	@Override
	public int insertRouteM(RouteM rm) {
		return sqlSession.insert("mapns.insertRm", rm);
	}
	@Override
	public int insertRouteS(RouteS rs) {
		return sqlSession.insert("mapns.insertRs", rs);
	}
	@Override
	public List<RouteS> getRouteList(RouteM r) {
		return sqlSession.selectList("mapns.routeList", r);
	}
}
