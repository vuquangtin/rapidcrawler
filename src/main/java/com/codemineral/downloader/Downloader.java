package com.codemineral.downloader;

import com.codemineral.core.Site;
import com.codemineral.http.Page;
import com.codemineral.http.Request;

public interface Downloader {
	Page download(Request request, Site site);

	void setThreadNum(int num);
}
