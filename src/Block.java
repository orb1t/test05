import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by alex on 12/29/2014.
 */


public class Block extends AsmLib {

	public String label;
	public Block parent;
	public Block child;
	public ArrayList<String> tokens = new ArrayList<String>();
	public ArrayList<Block> blocks = new ArrayList<Block>();

	public Block(String label, Block parent) {
		this.label = label;
		this.parent = parent;
	}

	public String toString() {
		StringBuilder out = new StringBuilder(label);
		if (!label.equals("")) out.append(" (");
		for (int i = 0 ; i < tokens.size() ; i ++) {
			if (tokens.get(i).startsWith("##")) {
				int index = Integer.parseInt(tokens.get(i).substring(2));
				out.append(blocks.get(index).toString());
			} else {
				out.append(tokens.get(i));
				if (i != tokens.size() - 1) out.append(",");
			}
		}
		if (!label.equals("")) out.append(")");
		if (child != null) {
			out.append(" {\n");
			out.append(child.toString());
			out.append("\n}");
		}
		return out.toString();
	}

	public String classToString() {
		StringBuilder out = new StringBuilder("class ");
		out.append(label);
		out.append(" {\n");
		for (int i = 0 ; i < tokens.size() ; i ++) {
			if (tokens.get(i).startsWith("##")) {
				int index = Integer.parseInt(tokens.get(i).substring(2));
				out.append(blocks.get(index).toString());
			} else {
				out.append(tokens.get(i));
				if (i != tokens.size() - 1) out.append(",");
			}
		}
		if (child != null) {
			out.append(" {\n");
			out.append(child.toString());
			out.append("\n}");
		}
		out.append("\n}");
		return out.toString();
	}

	byte[] asClass() {
		init();
		AsmClass out = new AsmClass(label);
		ArrayList<Byte> code = new ArrayList<Byte>();
		for (int i = 0 ; i < tokens.size() ; i ++) {
			if (tokens.get(i).startsWith("##")) {
				int index = Integer.parseInt(tokens.get(i).substring(2));
				Block block = blocks.get(index);
				String returnType = tokens.get(i - 1);
				String methodHeader =  block.label;
				for (int j = i - 2 ; j >= i - 4 && j >= 0 ; j --) {
					if (tokens.get(j).matches("public|private|protected|static|final|volatile|transient|enum")) {
						methodHeader = tokens.get(j) + ' ' + methodHeader;
					} else {
						break;
					}
				}
				methodHeader = methodHeader + '(';
				for (int j = 0 ; j < block.tokens.size() ; j += 2) {
					String type = getReturnType(block.tokens.get(j));
					methodHeader = methodHeader + type;
				}
				methodHeader = methodHeader + ')';
				returnType = getReturnType(returnType);
				methodHeader = methodHeader + returnType;
				Method method = new Method(methodHeader);
				hasLocals.set(true);
				locals.set((block.tokens.size() / 2) + 1);
				ArrayList<Byte> methodCode = method.create(block.asMethod());
				code.addAll(methodCode);
			}
		}
		return out.create(code);
	}

	private ArrayList<Byte> asMethod() {
		ArrayList<Byte> out = new ArrayList<Byte>();
		if (child != null) for (int i = 0 ; i < child.tokens.size() ; i ++) {
			if (child.tokens.get(i).startsWith("##")) {
				int index = Integer.parseInt(child.tokens.get(i).substring(2));
				Block block = child.blocks.get(index);
				if (block.label.equals("println")) {
					String token = block.tokens.get(0);
					if (token.startsWith("\"") && token.endsWith("\"")) {
						out.addAll(println(string(token.substring(1, token.length() - 1))));
					}
				} else if (block.label.equals("print")) {
					String token = block.tokens.get(0);
					if (token.startsWith("\"") && token.endsWith("\"")) {
						out.addAll(print(string(token.substring(1, token.length() - 1))));
					}
				}
			}
		}
		return out;
	}

	private String getReturnType(String returnType) {
		if (returnType.equals("void")) return "V";
		else if (returnType.equals("byte")) return "B";
		else if (returnType.equals("char")) return "C";
		else if (returnType.equals("double")) return "D";
		else if (returnType.equals("float")) return "F";
		else if (returnType.equals("int")) return "I";
		else if (returnType.equals("long")) return "J";
		else if (returnType.equals("short")) return "S";
		else if (returnType.equals("boolean")) return "Z";
		else if (returnType.endsWith("[]")){
			return '[' + getReturnType(returnType.substring(0, returnType.length() - 2));
		} else {
			return 'L' + classLookup(returnType) + ';';
		}
	}

	public static File classDictionary;
	static {
		String os = System.getenv("OS");
		if (os.contains("Windows")) {
			for (String path : System.getenv("PATH").split(";")) {
				if (path.contains("Java")) {
					File file = new File(new File(path.replaceAll("\\\\bin$", ""), "lib"), "rt.jar");
					if (file.exists()) {
						classDictionary = file;
						break;
					}
				}
			}
		}
	}

	private String classLookup(String className) {
		try {
			JarFile jarFile = new JarFile(classDictionary);
			Enumeration<JarEntry> entries = jarFile.entries();
			ArrayList<String> options = new ArrayList<String>();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName();
				if (name.endsWith("/" + className + ".class")) {
					options.add(name);
				}
			}
			jarFile.close();
			for (String option : options) {
				if (option.startsWith("java/lang")) {
					return option.replaceAll(".class", "");
				}
			}
			return options.get(0).replaceAll(".class", "");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
