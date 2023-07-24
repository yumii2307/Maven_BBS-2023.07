package utility;

import java.util.ArrayList;
import java.util.List;

public class JSONTest {

	public static void main(String[] args) {
		JsonUtil ju = new JsonUtil();
		List<String> list = new ArrayList<>();
		list.add("swimming.jpg"); list.add("고양이.jpg"); 
		list.add("글로리.jpg"); list.add("수영.jpg");
		String jStr = ju.listToJson(list);
		System.out.println(jStr + jStr.length());
		
		list = ju.jsonToList(jStr);
		list.forEach(x -> System.out.println(x));
	}

}
