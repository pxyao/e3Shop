package snippet;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import pojo.Student;

public class Snippet {
	@Test
	public void genFile() throws Exception {
		// 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 第二步：设置模板文件所在的路径。
		configuration.setDirectoryForTemplateLoading(
				new File("E:/Programing/Project/webtest/e3Shop/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		// 第三步：设置模板文件使用的字符集。一般就是utf-8.
		configuration.setDefaultEncoding("utf-8");
		// 第四步：加载一个模板，创建一个模板对象。
//		Template template = configuration.getTemplate("hello.ftl");
		Template template = configuration.getTemplate("student.ftl");
		// 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
		Map dataModel = new HashMap<>();
		// 向数据集中添加数据
		dataModel.put("hello", "this is my first freemarker test.");
		
		List<Student> students = new ArrayList<>();
		students.add(new Student(1,"N1",18,"XXX1"));
		students.add(new Student(2,"N2",18,"XXX2"));
		students.add(new Student(3,"N3",18,"XXX3"));
		students.add(new Student(4,"N4",18,"XXX4"));
		students.add(new Student(5,"N5",18,"XXX5"));
		students.add(new Student(6,"N6",18,"XXX6"));
		dataModel.put("studentList", students);
		Date date = new Date();
		dataModel.put("date", date);
		//null值的测试
		dataModel.put("val", null);
		// 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
		Writer out = new FileWriter(new File("D:/temp/list.html"));
		// 第七步：调用模板对象的process方法输出文件。
		template.process(dataModel, out);
		// 第八步：关闭流。
		out.close();
	}

}
