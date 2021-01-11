package utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestCaseReport {
	
	BufferedWriter bw;
	String labelStr="";
	/**
	 * 
	 * @throws IOException
	 */
	public void crBegin() throws IOException {
		bw = new BufferedWriter(new FileWriter(new File("./Reports/TestCaseReport.html")));
		bw.write("<html> <style>\r\n" + 
				"table {border-collapse: collapse; width: 100%;}\r\n" + 
				"th, td {padding: 8px; text-align: left; border-bottom: 1px solid #ddd;}\r\n" + 
				"tr:hover {background-color:#f5f5f5;}\r\n" + 
				"th{background-color: #3A3948; color: white;}\r\n" + 
				"</style>" + "<table>\r\n" + "<tr>\r\n"
				+ "<th>Test Case Name</th>\r\n" + "<th>Starting Time</th>\r\n" + "<th>Ending Time</th>\r\n"
				+ "<th>More Details</th>\r\n" + "</tr>");
	}

	/**
	 * 
	 * @param tcName
	 * @param startTime
	 * @param endTime
	 * @param refer
	 * @throws IOException
	 */
	public void crMain(String tcName, String startTime, String endTime, String refer, String status) throws IOException {
		if(status.equals("Pass")) {
			labelStr += "{ label: \""+tcName+"\",  y: 1  },";
		}
		else if(status.equals("Fail")) {
			labelStr += "{ label: \""+tcName+"\",  y: -1  },";
		}
		bw.write("<tr>\r\n" + "<td>" + tcName + "</td>\r\n" + "<td>" + startTime + "</td>\r\n" + "<td>" + endTime
				+ "</td>\r\n" + "<td><a href=\"" + refer + "\">TestCase</a></td>\r\n" + "</tr>");
	}

	/**
	 * 
	 * @throws IOException
	 */
	public void crEnd() throws IOException {
		/*bw.write("</table>\r\n" + "</body>\r\n" + "</html>");
		bw.close();*/
		bw.write("</table><script type=\"text/javascript\">\r\n" + "window.onload = function () {\r\n" + "\r\n"
				+ "var chart = new CanvasJS.Chart(\"chartContainer\", {\r\n"
				+ "	theme: \"light1\", // \"light2\", \"dark1\", \"dark2\", \"light1\" etc.\r\n"
				+ "	animationEnabled: true, // change to true		\r\n" + "	title:{\r\n" + "		text: \"\"\r\n"
				+ "	},\r\n" + "	data: [\r\n" + "	{\r\n"
				+ "		// Change type to \"bar\", \"area\", \"spline\", \"pie\", \"column\"etc.\r\n"
				+ "		type: \"pie\",\r\n" + "		dataPoints: [" + labelStr  + "		]\r\n" + "	}\r\n"
				+ "	]\r\n" + "});\r\n" + "chart.render();\r\n" + "\r\n" + "}\r\n" + "</script>\r\n"
				+ "<div id=\"chartContainer\" style=\"height: 250px; width: 100%;\"></div>\r\n"
				+ "<script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"> </script></body></html>");
		bw.close();
	}

}
