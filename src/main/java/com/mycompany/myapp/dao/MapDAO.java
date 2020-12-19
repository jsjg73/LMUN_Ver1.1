package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.model.Place;
import com.mycompany.myapp.model.RouteM;
import com.mycompany.myapp.model.RouteS;

public interface MapDAO {
	
	public RouteM routeSearch(String id);

	public int insertRouteM(RouteM rm);

	public int insertRouteS(RouteS rs);

	public List<RouteS> getRouteList(RouteM r);

}
