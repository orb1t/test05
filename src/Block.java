import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
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
	public int byteLength;

	public Block(String label, Block parent) {
		this.label = label;
		this.parent = parent;
		this.byteLength = 0;
	}

	public String toString() {
		StringBuilder out = new StringBuilder(label);
		if (!label.equals("")) out.append(" (");
		for (int i = 0 ; i < tokens.size() ; i ++) {
			if (tokens.get(i).startsWith("##")) {
				int index = Integer.parseInt(tokens.get(i).substring(2));
				out.append(" {\n");
				out.append(blocks.get(index).toString());
				out.append("\n}");
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
				LinkedHashMap<String, String> argMap = new LinkedHashMap<String, String>();
				for (int j = 0 ; j < block.tokens.size() ; j += 2) {
					String type = getReturnType(block.tokens.get(j));
					String argName = block.tokens.get(j + 1);
					argMap.put(argName, type);
					methodHeader = methodHeader + type;
				}
				methodHeader = methodHeader + ')';
				returnType = getReturnType(returnType);
				methodHeader = methodHeader + returnType;
				Method method = new Method(methodHeader);
				hasLocals.set(true);
				locals.set((block.tokens.size() / 2) + 1);
				ArrayList<Byte> methodCode = method.create(block.asMethod(argMap));
				code.addAll(methodCode);
			}
		}
		return out.create(code);
	}

	private ArrayList<Byte> asMethod(LinkedHashMap<String, String> argMap) {
		LinkedHashMap<String, String> localMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> localIndex = new LinkedHashMap<String, Integer>();
		Stack<ArrayList<String>> blockLocals = new Stack<ArrayList<String>>();
		blockLocals.push(new ArrayList<String>());
		for (int i = 0 ; i < argMap.size() ; i ++) {
			String argName = argMap.keySet().toArray(new String[0])[i];
			localIndex.put(argName, i);
			localMap.put(argName, argMap.get(argName));
			blockLocals.peek().add(argName);
		}
		ArrayList<Byte> out = new ArrayList<Byte>();
		if (child != null) for (AtomicInteger i = new AtomicInteger(0) ; i.get() < child.tokens.size() ; i.getAndIncrement()) {
			ArrayList<Byte> bytes = translateToken(child.tokens.get(i.get()), i, localMap, localIndex, blockLocals);
			if (bytes.size() > 0) {
				out.addAll(bytes);
			}
			this.byteLength = out.size();
		}
		return out;
	}

	private ArrayList<Byte> translateToken(String thisToken, AtomicInteger tokenIndex, LinkedHashMap<String, String> localMap, LinkedHashMap<String, Integer> localIndex, Stack<ArrayList<String>> blockLocals) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		if (thisToken.startsWith("##")) {
			int index = Integer.parseInt(thisToken.substring(2));
			Block block = child.blocks.get(index);
			if (block.label.equals("println")) {
				String token = block.tokens.get(0);
				tokenIndex.getAndIncrement();
				out.addAll(println(translateToken(token, tokenIndex, localMap, localIndex, blockLocals)));
			} else if (block.label.equals("print")) {
				String token = block.tokens.get(0);
				tokenIndex.getAndIncrement();
				out.addAll(print(translateToken(token, tokenIndex, localMap, localIndex, blockLocals)));
			}
		} else if (thisToken.equals("=")) {
			String var = child.tokens.get(tokenIndex.get() - 1);
			String val = child.tokens.get(tokenIndex.get() + 1);
			tokenIndex.getAndIncrement();
			out.addAll(translateToken(val, tokenIndex, localMap, localIndex, blockLocals));
			localMap.put(var, returnValue.get());
			out.addAll(smartStore_(returnValue.get(), localIndex.size()));
			localIndex.put(var, localIndex.size());
			locals.set(locals.get() + 1);
		} else if (thisToken.startsWith("\"") && thisToken.endsWith("\"")) {
			out.addAll(string(thisToken.substring(1, thisToken.length() - 1)));
		} else if (thisToken.matches("^\\d+$")) {
			out.addAll(integer(Integer.parseInt(thisToken)));
		} else if (thisToken.equals("for")) {
			String var = child.tokens.get(tokenIndex.incrementAndGet());
			if (child.tokens.get(tokenIndex.incrementAndGet()).equals("in")) {
				String val = child.tokens.get(tokenIndex.incrementAndGet());
				if (val.startsWith("##")) {
					//
				} else {
					out.addAll(translateToken(val, tokenIndex, localMap, localIndex, blockLocals));
					localMap.put(var, returnValue.get());
					blockLocals.add(new ArrayList<String>());
					blockLocals.peek().add(var);
					out.addAll(smartStore_(returnValue.get(), localIndex.size()));
					localIndex.put(var, localIndex.size());
					locals.set(locals.get() + 1);
					String mod = child.tokens.get(tokenIndex.incrementAndGet());
					String max = child.tokens.get(tokenIndex.incrementAndGet());
					int label = this.byteLength + out.size();
					String blockLabel = child.tokens.get(tokenIndex.incrementAndGet());
					int increment = 1;
					if (blockLabel.equals("by")) {
						increment = Integer.parseInt(child.tokens.get(tokenIndex.incrementAndGet()));
						blockLabel = child.tokens.get(tokenIndex.incrementAndGet());
					}
					if (blockLabel.startsWith("##")) {
						int minVal = Integer.parseInt(val);
						int maxVal = Integer.parseInt(max);
						if (minVal < maxVal) {
							//out.addAll();
						} else {
							//out.addAll();
						} 
						int blockIndex = Integer.parseInt(blockLabel.substring(2));
						Block b = blocks.get(blockIndex);
						for (int i = 0 ; i < b.tokens.size() ; i ++) {
							AtomicInteger newIndex = new AtomicInteger(0);
							out.addAll(translateToken(b.tokens.get(i), newIndex, localMap, localIndex, blockLocals));
							System.out.println("");
						}
					}
				}
			}
		} else if (thisToken.equals("+")) {
		} else if (thisToken.equals("-")) {
		} else if (thisToken.equals("/")) {
		} else if (thisToken.equals("*")) {
		} else if (thisToken.equals("%")) {
		} else if (thisToken.equals("++")) {
		} else if (thisToken.equals("--")) {
		} else if (thisToken.equals("&&")) {
		} else if (thisToken.equals("||")) {
		} else if (thisToken.equals("&")) {
		} else if (thisToken.equals("|")) {
		} else if (thisToken.equals("^")) {
		} else if (localMap.containsKey(thisToken)) {
			//ArrayList<Byte> bytes = aload_(localIndex.get(thisToken));
			String loadType = localMap.get(thisToken);
			ArrayList<Byte> bytes = smartLoad_(loadType, localIndex.get(thisToken));
			returnValue.set(localMap.get(thisToken));
			out.addAll(bytes);
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
