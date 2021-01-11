package executeEngine;

import java.util.LinkedHashMap;

public class CommonFunction {

static LinkedHashMap<String,String> map;
	
	public static LinkedHashMap<String, String> objToMap(Object[][] module) {
//		String str = Arrays.deepToString(module);
		
//		System.out.println(str);
		
		map = new LinkedHashMap<String,String>();
		for(int i=0;i<module.length;i++) {
			map.put(module[i][1].toString(), module[i][2].toString());
		}
		
	return map;
	}
	
}
