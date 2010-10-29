package org.phxandroid.examples.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.phxandroid.examples.utils.IOUtil;

public class HtmlPage {
	private List<String> links = new ArrayList<String>();
	private String title;

	public HtmlPage(HttpEntity entity) throws IOException {
		InputStream in = null;
		InputStreamReader reader = null;
		BufferedReader buf = null;
		try {
			in = entity.getContent();
			reader = new InputStreamReader(in);
			buf = new BufferedReader(reader);

			Pattern titlepat = Pattern.compile("<title>([^<]*)</title>");
			Pattern linkpat = Pattern.compile("href=\"([^\"]*)\"");
			Matcher mat;

			String line, link;
			while ((line = buf.readLine()) != null) {
				mat = titlepat.matcher(line);
				if (mat.find()) {
					title = mat.group(1);
				}
				mat = linkpat.matcher(line);
				if (mat.find()) {
					link = mat.group(1);
					links.add(link);
				}
			}
		} finally {
			IOUtil.close(buf);
			IOUtil.close(reader);
			IOUtil.close(in);
		}
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "HtmlPage [links.size=" + links.size() + "]";
	}

	public List<String> getLinks() {
		return links;
	}

	public List<String> getLinksRegex(String regex) {
		List<String> ret = new ArrayList<String>();
		Pattern linkpat = Pattern.compile(regex);

		for (String link : links) {
			if (linkpat.matcher(link).matches()) {
				ret.add(link);
			}
		}

		return ret;
	}
}
