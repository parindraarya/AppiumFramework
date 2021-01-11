package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomReport {

	static int j = 1;
	BufferedWriter bw;
	String labelStr="";

	/**
	 * 
	 * @throws IOException
	 */
	public void reportBegin() throws IOException {
		bw = new BufferedWriter(new FileWriter(new File("./Reports/Custom/TestCase" + j + ".html")));
		bw.write("<html>\r\n" + "<head>\r\n<style>\r\n" + "table {\r\n" + "    border-collapse: collapse;\r\n"
				+ "    width: 100%;\r\n" + "}\r\n" + "\r\n" + "th, td {\r\n" + "    padding: 8px;\r\n"
				+ "    text-align: left;\r\n" + "    border-bottom: 1px solid #ddd;\r\n"
				+ "}\r\n tr:hover {background-color:#f5f5f5;}" + "th{background-color: #3A3948;\r\n"
				+ "    color: white;}" + "</style>" + "<title>Test HTML Report</title>\r\n" + "</head>\r\n"
				+ "<body>\r\n" + "<h3>Test Report</h3>\r\n" + "<table>\r\n" + "<tr>\r\n"
				+ "<th width=\"20%\">Testcase</th>\r\n" + "<th width=\"30%\">Action</th>\r\n"
				+ "<th width=\"20%\">Status</th>\r\n" + "<th width=\"30%\">Screenshot</th>\r\n" + "</tr>\r\n");
	}

	/**
	 * 
	 * @param tcName
	 * @param action
	 * @param status
	 * @param path
	 * @throws IOException
	 */
	public void reportMain(String tcName, String action, String status, String path) throws IOException {
		if(status.equals("Pass")) {
			labelStr += "{ label: \""+tcName+"\",  y: 1  },";
		}
		else if(status.equals("Fail")) {
			labelStr += "{ label: \""+tcName+"\",  y: -1  },";
		}
		System.out.println("Reporter " + action + " " + status + " " + path);
		if (action != null && status != null && path != null) {
			bw.write("<tr>\r\n" + "<td> " + tcName + " </td>\r\n" + "<td> " + action + " </td>\r\n" + "<td> " + status
					+ " </td>\r\n" + "<td><a href=\"" + path + "\">Screenshot</a></td>\r\n" + "</tr>");
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void reportEnd() throws IOException {
		bw.write("</table><script type=\"text/javascript\">\r\n" + 
				"window.onload = function () {\r\n" + 
				"\r\n" + 
				"var chart = new CanvasJS.Chart(\"chartContainer\", {\r\n" + 
				"	theme: \"light1\", // \"light2\", \"dark1\", \"dark2\", \"light1\" etc.\r\n" + 
				"	animationEnabled: true, // change to true		\r\n" + 
				"	title:{\r\n" + 
				"		text: \"\"\r\n" + 
				"	},\r\n" + 
				"	data: [\r\n" + 
				"	{\r\n" + 
				"		// Change type to \"bar\", \"area\", \"spline\", \"pie\", \"column\"etc.\r\n" + 
				"		type: \"bar\",\r\n" + 
				"		dataPoints: [" +labelStr + 
				"		]\r\n" + 
				"	}\r\n" + 
				"	]\r\n" + 
				"});\r\n" + 
				"chart.render();\r\n" + 
				"\r\n" + 
				"}\r\n" + 
				"</script>\r\n" + 
				"<div id=\"chartContainer\" style=\"height: 250px; width: 100%;\"></div>\r\n" + 
				"<script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"> </script>" + "</body></html>");
		j++;
		bw.close();
	}
	
}
