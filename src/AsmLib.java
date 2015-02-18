import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.util.*;

/**
 * Created by alex on 12/8/2014.
 */
public class AsmLib {

	private static HashMap<Integer, String> first = new HashMap<Integer, String>() {{
		put(0x32, "aaload");
		put(0x53, "aastore");
		put(0x01, "aconst_null");
		put(0x19, "aload");
		put(0x2a, "aload_0");
		put(0x2b, "aload_1");
		put(0x2c, "aload_2");
		put(0x2d, "aload_3");
		put(0xbd, "anewarray");
		put(0xb0, "areturn");
		put(0xbe, "arraylength");
		put(0x3a, "astore");
		put(0x4b, "astore_0");
		put(0x4c, "astore_1");
		put(0x4d, "astore_2");
		put(0x4e, "astore_3");
		put(0xbf, "athrow");
		put(0x33, "baload");
		put(0x54, "bastore");
		put(0x10, "bipush");
		put(0xca, "breakpoint");
		put(0x34, "caload");
		put(0x55, "castore");
		put(0xc0, "checkcast");
		put(0x90, "d2f");
		put(0x8e, "d2i");
		put(0x8f, "d2l");
		put(0x63, "dadd");
		put(0x31, "daload");
		put(0x52, "dastore");
		put(0x98, "dcmpg");
		put(0x97, "dcmpl");
		put(0x0e, "dconst_0");
		put(0x0f, "dconst_1");
		put(0x6f, "ddiv");
		put(0x18, "dload");
		put(0x26, "dload_0");
		put(0x27, "dload_1");
		put(0x28, "dload_2");
		put(0x29, "dload_3");
		put(0x6b, "dmul");
		put(0x77, "dneg");
		put(0x73, "drem");
		put(0xaf, "dreturn");
		put(0x39, "dstore");
		put(0x47, "dstore_0");
		put(0x48, "dstore_1");
		put(0x49, "dstore_2");
		put(0x4a, "dstore_3");
		put(0x67, "dsub");
		put(0x59, "dup");
		put(0x5a, "dup_x1");
		put(0x5b, "dup_x2");
		put(0x5c, "dup2");
		put(0x5d, "dup2_x1");
		put(0x5e, "dup2_x2");
		put(0x8d, "f2d");
		put(0x8b, "f2i");
		put(0x8c, "f2l");
		put(0x62, "fadd");
		put(0x30, "faload");
		put(0x51, "fastore");
		put(0x96, "fcmpg");
		put(0x95, "fcmpl");
		put(0x0b, "fconst_0");
		put(0x0c, "fconst_1");
		put(0x0d, "fconst_2");
		put(0x6e, "fdiv");
		put(0x17, "fload");
		put(0x22, "fload_0");
		put(0x23, "fload_1");
		put(0x24, "fload_2");
		put(0x25, "fload_3");
		put(0x6a, "fmul");
		put(0x76, "fneg");
		put(0x72, "frem");
		put(0xae, "freturn");
		put(0x38, "fstore");
		put(0x43, "fstore_0");
		put(0x44, "fstore_1");
		put(0x45, "fstore_2");
		put(0x46, "fstore_3");
		put(0x66, "fsub");
		put(0xb4, "getfield");
		put(0xb2, "getstatic");
		put(0xa7, "goto");
		put(0xc8, "goto_w");
		put(0x91, "i2b");
		put(0x92, "i2c");
		put(0x87, "i2d");
		put(0x86, "i2f");
		put(0x85, "i2l");
		put(0x93, "i2s");
		put(0x60, "iadd");
		put(0x2e, "iaload");
		put(0x7e, "iand");
		put(0x4f, "iastore");
		put(0x02, "iconst_m1");
		put(0x03, "iconst_0");
		put(0x04, "iconst_1");
		put(0x05, "iconst_2");
		put(0x06, "iconst_3");
		put(0x07, "iconst_4");
		put(0x08, "iconst_5");
		put(0x6c, "idiv");
		put(0xa5, "if_acmpeq");
		put(0xa6, "if_acmpne");
		put(0x9f, "if_icmpeq");
		put(0xa2, "if_icmpge");
		put(0xa3, "if_icmpgt");
		put(0xa4, "if_icmple");
		put(0xa1, "if_icmplt");
		put(0xa0, "if_icmpne");
		put(0x99, "ifeq");
		put(0x9c, "ifge");
		put(0x9d, "ifgt");
		put(0x9e, "ifle");
		put(0x9b, "iflt");
		put(0x9a, "ifne");
		put(0xc7, "ifnonnull");
		put(0xc6, "ifnull");
		put(0x84, "iinc");
		put(0x15, "iload");
		put(0x1a, "iload_0");
		put(0x1b, "iload_1");
		put(0x1c, "iload_2");
		put(0x1d, "iload_3");
		put(0xfe, "impdep1");
		put(0xff, "impdep2");
		put(0x68, "imul");
		put(0x74, "ineg");
		put(0xc1, "instanceof");
		put(0xba, "invokedynamic");
		put(0xb9, "invokeinterface");
		put(0xb7, "invokespecial");
		put(0xb8, "invokestatic");
		put(0xb6, "invokevirtual");
		put(0x80, "ior");
		put(0x70, "irem");
		put(0xac, "ireturn");
		put(0x78, "ishl");
		put(0x7a, "ishr");
		put(0x36, "istore");
		put(0x3b, "istore_0");
		put(0x3c, "istore_1");
		put(0x3d, "istore_2");
		put(0x3e, "istore_3");
		put(0x64, "isub");
		put(0x7c, "iushr");
		put(0x82, "ixor");
		put(0xa8, "jsr");
		put(0xc9, "jsr_w");
		put(0x8a, "l2d");
		put(0x89, "l2f");
		put(0x88, "l2i");
		put(0x61, "ladd");
		put(0x2f, "laload");
		put(0x7f, "land");
		put(0x50, "lastore");
		put(0x94, "lcmp");
		put(0x09, "lconst_0");
		put(0x0a, "lconst_1");
		put(0x12, "ldc");
		put(0x13, "ldc_w");
		put(0x14, "ldc2_w");
		put(0x6d, "ldiv");
		put(0x16, "lload");
		put(0x1e, "lload_0");
		put(0x1f, "lload_1");
		put(0x20, "lload_2");
		put(0x21, "lload_3");
		put(0x69, "lmul");
		put(0x75, "lneg");
		put(0xab, "lookupswitch");
		put(0x81, "lor");
		put(0x71, "lrem");
		put(0xad, "lreturn");
		put(0x79, "lshl");
		put(0x7b, "lshr");
		put(0x37, "lstore");
		put(0x3f, "lstore_0");
		put(0x40, "lstore_1");
		put(0x41, "lstore_2");
		put(0x42, "lstore_3");
		put(0x65, "lsub");
		put(0x7d, "lushr");
		put(0x83, "lxor");
		put(0xc2, "monitorenter");
		put(0xc3, "monitorexit");
		put(0xc5, "multianewarray");
		put(0xbb, "new");
		put(0xbc, "newarray");
		put(0x00, "nop");
		put(0x57, "pop");
		put(0x58, "pop2");
		put(0xb5, "putfield");
		put(0xb3, "putstatic");
		put(0xa9, "ret");
		put(0xb1, "return");
		put(0x35, "saload");
		put(0x56, "sastore");
		put(0x11, "sipush");
		put(0x5f, "swap");
		put(0xaa, "tableswitch");
	}};
	private static HashMap<String, Integer> last = new HashMap<String, Integer>() {{
		put("aaload", 0x32);
		put("aastore", 0x53);
		put("aconst_null", 0x01);
		put("aload", 0x19);
		put("aload_0", 0x2a);
		put("aload_1", 0x2b);
		put("aload_2", 0x2c);
		put("aload_3", 0x2d);
		put("anewarray", 0xbd);
		put("areturn", 0xb0);
		put("arraylength", 0xbe);
		put("astore", 0x3a);
		put("astore_0", 0x4b);
		put("astore_1", 0x4c);
		put("astore_2", 0x4d);
		put("astore_3", 0x4e);
		put("athrow", 0xbf);
		put("baload", 0x33);
		put("bastore", 0x54);
		put("bipush", 0x10);
		put("breakpoint", 0xca);
		put("caload", 0x34);
		put("castore", 0x55);
		put("checkcast", 0xc0);
		put("d2f", 0x90);
		put("d2i", 0x8e);
		put("d2l", 0x8f);
		put("dadd", 0x63);
		put("daload", 0x31);
		put("dastore", 0x52);
		put("dcmpg", 0x98);
		put("dcmpl", 0x97);
		put("dconst_0", 0x0e);
		put("dconst_1", 0x0f);
		put("ddiv", 0x6f);
		put("dload", 0x18);
		put("dload_0", 0x26);
		put("dload_1", 0x27);
		put("dload_2", 0x28);
		put("dload_3", 0x29);
		put("dmul", 0x6b);
		put("dneg", 0x77);
		put("drem", 0x73);
		put("dreturn", 0xaf);
		put("dstore", 0x39);
		put("dstore_0", 0x47);
		put("dstore_1", 0x48);
		put("dstore_2", 0x49);
		put("dstore_3", 0x4a);
		put("dsub", 0x67);
		put("dup", 0x59);
		put("dup_x1", 0x5a);
		put("dup_x2", 0x5b);
		put("dup2", 0x5c);
		put("dup2_x1", 0x5d);
		put("dup2_x2", 0x5e);
		put("f2d", 0x8d);
		put("f2i", 0x8b);
		put("f2l", 0x8c);
		put("fadd", 0x62);
		put("faload", 0x30);
		put("fastore", 0x51);
		put("fcmpg", 0x96);
		put("fcmpl", 0x95);
		put("fconst_0", 0x0b);
		put("fconst_1", 0x0c);
		put("fconst_2", 0x0d);
		put("fdiv", 0x6e);
		put("fload", 0x17);
		put("fload_0", 0x22);
		put("fload_1", 0x23);
		put("fload_2", 0x24);
		put("fload_3", 0x25);
		put("fmul", 0x6a);
		put("fneg", 0x76);
		put("frem", 0x72);
		put("freturn", 0xae);
		put("fstore", 0x38);
		put("fstore_0", 0x43);
		put("fstore_1", 0x44);
		put("fstore_2", 0x45);
		put("fstore_3", 0x46);
		put("fsub", 0x66);
		put("getfield", 0xb4);
		put("getstatic", 0xb2);
		put("goto", 0xa7);
		put("goto_w", 0xc8);
		put("i2b", 0x91);
		put("i2c", 0x92);
		put("i2d", 0x87);
		put("i2f", 0x86);
		put("i2l", 0x85);
		put("i2s", 0x93);
		put("iadd", 0x60);
		put("iaload", 0x2e);
		put("iand", 0x7e);
		put("iastore", 0x4f);
		put("iconst_m1", 0x02);
		put("iconst_0", 0x03);
		put("iconst_1", 0x04);
		put("iconst_2", 0x05);
		put("iconst_3", 0x06);
		put("iconst_4", 0x07);
		put("iconst_5", 0x08);
		put("idiv", 0x6c);
		put("if_acmpeq", 0xa5);
		put("if_acmpne", 0xa6);
		put("if_icmpeq", 0x9f);
		put("if_icmpge", 0xa2);
		put("if_icmpgt", 0xa3);
		put("if_icmple", 0xa4);
		put("if_icmplt", 0xa1);
		put("if_icmpne", 0xa0);
		put("ifeq", 0x99);
		put("ifge", 0x9c);
		put("ifgt", 0x9d);
		put("ifle", 0x9e);
		put("iflt", 0x9b);
		put("ifne", 0x9a);
		put("ifnonnull", 0xc7);
		put("ifnull", 0xc6);
		put("iinc", 0x84);
		put("iload", 0x15);
		put("iload_0", 0x1a);
		put("iload_1", 0x1b);
		put("iload_2", 0x1c);
		put("iload_3", 0x1d);
		put("impdep1", 0xfe);
		put("impdep2", 0xff);
		put("imul", 0x68);
		put("ineg", 0x74);
		put("instanceof", 0xc1);
		put("invokedynamic", 0xba);
		put("invokeinterface", 0xb9);
		put("invokespecial", 0xb7);
		put("invokestatic", 0xb8);
		put("invokevirtual", 0xb6);
		put("ior", 0x80);
		put("irem", 0x70);
		put("ireturn", 0xac);
		put("ishl", 0x78);
		put("ishr", 0x7a);
		put("istore", 0x36);
		put("istore_0", 0x3b);
		put("istore_1", 0x3c);
		put("istore_2", 0x3d);
		put("istore_3", 0x3e);
		put("isub", 0x64);
		put("iushr", 0x7c);
		put("ixor", 0x82);
		put("jsr", 0xa8);
		put("jsr_w", 0xc9);
		put("l2d", 0x8a);
		put("l2f", 0x89);
		put("l2i", 0x88);
		put("ladd", 0x61);
		put("laload", 0x2f);
		put("land", 0x7f);
		put("lastore", 0x50);
		put("lcmp", 0x94);
		put("lconst_0", 0x09);
		put("lconst_1", 0x0a);
		put("ldc", 0x12);
		put("ldc_w", 0x13);
		put("ldc2_w", 0x14);
		put("ldiv", 0x6d);
		put("lload", 0x16);
		put("lload_0", 0x1e);
		put("lload_1", 0x1f);
		put("lload_2", 0x20);
		put("lload_3", 0x21);
		put("lmul", 0x69);
		put("lneg", 0x75);
		put("lookupswitch", 0xab);
		put("lor", 0x81);
		put("lrem", 0x71);
		put("lreturn", 0xad);
		put("lshl", 0x79);
		put("lshr", 0x7b);
		put("lstore", 0x37);
		put("lstore_0", 0x3f);
		put("lstore_1", 0x40);
		put("lstore_2", 0x41);
		put("lstore_3", 0x42);
		put("lsub", 0x65);
		put("lushr", 0x7d);
		put("lxor", 0x83);
		put("monitorenter", 0xc2);
		put("monitorexit", 0xc3);
		put("multianewarray", 0xc5);
		put("new", 0xbb);
		put("newarray", 0xbc);
		put("nop", 0x00);
		put("pop", 0x57);
		put("pop2", 0x58);
		put("putfield", 0xb5);
		put("putstatic", 0xb3);
		put("ret", 0xa9);
		put("return", 0xb1);
		put("saload", 0x35);
		put("sastore", 0x56);
		put("sipush", 0x11);
		put("swap", 0x5f);
		put("tableswitch", 0xaa);
	}};
	private static HashMap<Integer, Integer> extra = new HashMap<Integer, Integer>() {{
		put(0x32, 0);
		put(0x53, 0);
		put(0x01, 0);
		put(0x19, 1);
		put(0x2a, 0);
		put(0x2b, 0);
		put(0x2c, 0);
		put(0x2d, 0);
		put(0xbd, 2);
		put(0xb0, 0);
		put(0xbe, 0);
		put(0x3a, 1);
		put(0x4b, 0);
		put(0x4c, 0);
		put(0x4d, 0);
		put(0x4e, 0);
		put(0xbf, 0);
		put(0x33, 0);
		put(0x54, 0);
		put(0x10, 1);
		put(0xca, 0);
		put(0x34, 0);
		put(0x55, 0);
		put(0xc0, 2);
		put(0x90, 0);
		put(0x8e, 0);
		put(0x8f, 0);
		put(0x63, 0);
		put(0x31, 0);
		put(0x52, 0);
		put(0x98, 0);
		put(0x97, 0);
		put(0x0e, 0);
		put(0x0f, 0);
		put(0x6f, 0);
		put(0x18, 1);
		put(0x26, 0);
		put(0x27, 0);
		put(0x28, 0);
		put(0x29, 0);
		put(0x6b, 0);
		put(0x77, 0);
		put(0x73, 0);
		put(0xaf, 0);
		put(0x39, 1);
		put(0x47, 0);
		put(0x48, 0);
		put(0x49, 0);
		put(0x4a, 0);
		put(0x67, 0);
		put(0x59, 0);
		put(0x5a, 0);
		put(0x5b, 0);
		put(0x5c, 0);
		put(0x5d, 0);
		put(0x5e, 0);
		put(0x8d, 0);
		put(0x8b, 0);
		put(0x8c, 0);
		put(0x62, 0);
		put(0x30, 0);
		put(0x51, 0);
		put(0x96, 0);
		put(0x95, 0);
		put(0x0b, 0);
		put(0x0c, 0);
		put(0x0d, 0);
		put(0x6e, 0);
		put(0x17, 1);
		put(0x22, 0);
		put(0x23, 0);
		put(0x24, 0);
		put(0x25, 0);
		put(0x6a, 0);
		put(0x76, 0);
		put(0x72, 0);
		put(0xae, 0);
		put(0x38, 1);
		put(0x43, 0);
		put(0x44, 0);
		put(0x45, 0);
		put(0x46, 0);
		put(0x66, 0);
		put(0xb4, 2);
		put(0xb2, 2);
		put(0xa7, 2);
		put(0xc8, 4);
		put(0x91, 0);
		put(0x92, 0);
		put(0x87, 0);
		put(0x86, 0);
		put(0x85, 0);
		put(0x93, 0);
		put(0x60, 0);
		put(0x2e, 0);
		put(0x7e, 0);
		put(0x4f, 0);
		put(0x02, 0);
		put(0x03, 0);
		put(0x04, 0);
		put(0x05, 0);
		put(0x06, 0);
		put(0x07, 0);
		put(0x08, 0);
		put(0x6c, 0);
		put(0xa5, 2);
		put(0xa6, 2);
		put(0x9f, 2);
		put(0xa2, 2);
		put(0xa3, 2);
		put(0xa4, 2);
		put(0xa1, 2);
		put(0xa0, 2);
		put(0x99, 2);
		put(0x9c, 2);
		put(0x9d, 2);
		put(0x9e, 2);
		put(0x9b, 2);
		put(0x9a, 2);
		put(0xc7, 2);
		put(0xc6, 2);
		put(0x84, 2);
		put(0x15, 1);
		put(0x1a, 0);
		put(0x1b, 0);
		put(0x1c, 0);
		put(0x1d, 0);
		put(0xfe, 0);
		put(0xff, 0);
		put(0x68, 0);
		put(0x74, 0);
		put(0xc1, 2);
		put(0xba, 4);
		put(0xb9, 4);
		put(0xb7, 2);
		put(0xb8, 2);
		put(0xb6, 2);
		put(0x80, 0);
		put(0x70, 0);
		put(0xac, 0);
		put(0x78, 0);
		put(0x7a, 0);
		put(0x36, 1);
		put(0x3b, 0);
		put(0x3c, 0);
		put(0x3d, 0);
		put(0x3e, 0);
		put(0x64, 0);
		put(0x7c, 0);
		put(0x82, 0);
		put(0xa8, 2);
		put(0xc9, 4);
		put(0x8a, 0);
		put(0x89, 0);
		put(0x88, 0);
		put(0x61, 0);
		put(0x2f, 0);
		put(0x7f, 0);
		put(0x50, 0);
		put(0x94, 0);
		put(0x09, 0);
		put(0x0a, 0);
		put(0x12, 1);
		put(0x13, 2);
		put(0x14, 2);
		put(0x6d, 0);
		put(0x16, 1);
		put(0x1e, 0);
		put(0x1f, 0);
		put(0x20, 0);
		put(0x21, 0);
		put(0x69, 0);
		put(0x75, 0);
		put(0xab, 4);
		put(0x81, 0);
		put(0x71, 0);
		put(0xad, 0);
		put(0x79, 0);
		put(0x7b, 0);
		put(0x37, 1);
		put(0x3f, 0);
		put(0x40, 0);
		put(0x41, 0);
		put(0x42, 0);
		put(0x65, 0);
		put(0x7d, 0);
		put(0x83, 0);
		put(0xc2, 0);
		put(0xc3, 0);
		put(0xc5, 3);
		put(0xbb, 2);
		put(0xbc, 1);
		put(0x00, 0);
		put(0x57, 0);
		put(0x58, 0);
		put(0xb5, 2);
		put(0xb3, 2);
		put(0xa9, 1);
		put(0xb1, 0);
		put(0x35, 0);
		put(0x56, 0);
		put(0x11, 2);
		put(0x5f, 0);
		put(0xaa, 4);
	}};

	public final static byte
			class_ = 7      // 2
			, string_ = 8
			, integer = 3   // 4
			, float_ = 4
			, fieldRef = 9
			, methodRef = 10
			, interfaceMethodRef = 11
			, nameAndType = 12
			, long_ = 5     // 8
			, double_ = 6
			, utf8 = 1;     // 2 + length
	public final static String[] typeNames = new String[]{
			""
			, "utf-8"
			, ""
			, "int"
			, "float"
			, "long"
			, "double"
			, "class"
			, "string_"
			, "fieldRef"
			, "methodRef"
			, "interfaceMethodRef"
			, "nameAndType"};
	public final static int[] typeSize = new int[]{
			0
			, 2 // + length
			, 0
			, 4
			, 4
			, 8
			, 8
			, 2
			, 2
			, 4
			, 4
			, 4
			, 4};
	public final static int
			public_ = 0x0001
			, private_ = 0x0002
			, protected_ = 0x0004
			, static_ = 0x0008
			, final_ = 0x0010
			, voatile = 0x0080
			, transient_ = 0x1000
			, enum_ = 0x4000;
	public final static int
			classPublic = 0x0001
			, classFinal = 0x0010
			, classSuper = 0x0020
			, classInterface = 0x0200
			, classAbstract = 0x0400
			, classSynthetic = 0x1000
			, classAnnotation = 0x2000
			, classEnum = 0x4000;

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("You must specify a input file!");
			System.exit(0);
		}
		String filename = args[0];
		try {
			if (!new File(filename).exists()) {
				System.err.println("The file specified does not exist!");
				System.exit(0);
			}

			InputStreamReader input = new InputStreamReader(new FileInputStream(filename));
			boolean whitespace = false;
			boolean quotes = false;
			boolean paren = false;
			boolean comment = false;

			ArrayList<Block> blocks = new ArrayList<Block>();
			ArrayList<Block> blocksDone = new ArrayList<Block>();
			String className = filename.replaceAll("\\.[^\\.]*$", "");
			blocks.add(new Block(className, null));
			StringBuffer buffer = new StringBuffer();

			int c = -1;
			while ((c = input.read()) != -1) {
				if (!quotes && (comment || c == ' ' || c == '\n' || c == '\r' || c == '\t')) {
					if (!whitespace) {
						if (buffer.length() > 0) {
							if (buffer.toString().startsWith("//")) {
								comment = true;
							} else {
								blocks.get(blocks.size() - 1).tokens.add(buffer.toString());
							}
						}
					}
					if (c == '\n') {
						comment = false;
					}
					whitespace = true;
				} else {
					if (whitespace) {
						buffer = new StringBuffer();
						whitespace = false;
					}
					if (c == '(') {
						if (buffer.length() > 0) {
							blocks.add(new Block(buffer.toString(), blocks.get(blocks.size() - 1)));
						} else {
							Block last = blocks.get(blocks.size() - 1);
							blocks.add(new Block(last.tokens.remove(last.tokens.size() - 1), blocks.get(blocks.size() - 1)));
						}
						blocks.get(blocks.size() - 2).tokens.add("##" + blocks.get(blocks.size() - 1).parent.blocks.size());
						blocks.get(blocks.size() - 1).parent.blocks.add(blocks.get(blocks.size() - 1));
						whitespace = true;
					} else if (c == ')') {
						if (buffer.length() > 0) {
							blocks.get(blocks.size() - 1).tokens.add(buffer.toString());
						}
						blocksDone.add(blocks.remove(blocks.size() - 1));
						whitespace = true;
						paren = true;
					} else if (c == '{') {
						Block parent;
						if (paren) {
							parent = blocksDone.get(blocksDone.size() - 1);
							Block last = blocks.get(blocks.size() - 1);
							blocks.add(new Block("", parent));
							parent.child = blocks.get(blocks.size() - 1);
							whitespace = true;
						} else {
							parent = blocks.get(blocks.size() - 1);
							parent.tokens.add("##" + parent.blocks.size());
							//buffer = new StringBuffer("##" + blocks.size());
							blocks.add(new Block(buffer.toString(), parent));
							parent.blocks.add(blocks.get(blocks.size() - 1));
						}
						paren = false;
					} else if (c == '}') {
						if (buffer.length() > 0) {
							blocks.get(blocks.size() - 1).tokens.add(buffer.toString());
						}
						//blocks.get(blocks.size() - 1).parent.blocks.add(blocks.get(blocks.size() - 1));
						blocksDone.add(blocks.remove(blocks.size() - 1));
						whitespace = true;
					} else if (c == '"') {
						paren = false;
						if (quotes) {
							quotes = false;
						} else {
							quotes = true;
						}
						buffer.append((char) c);
					} else {
						paren = false;
						buffer.append((char) c);
					}
				}
			}
			input.close();

			System.out.println(blocks.get(0).classToString());
			byte[] clazz = blocks.get(0).asClass();
			FileOutputStream fileOutputStream = new FileOutputStream(new File(className + ".class"));
			fileOutputStream.write(clazz);
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, byte[]> clazzByteMap = new HashMap<String, byte[]>();
	public static HashMap<String, Class<?>> clazzMap = new HashMap<String, Class<?>>();

	public class CL extends URLClassLoader {

		public CL(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			if (clazzMap.containsKey(name)) {
				return clazzMap.get(name);
			}else if (clazzByteMap.containsKey(name)) {
				Class<?> out = defineClass(name, clazzByteMap.get(name), 0, clazzByteMap.size());
				clazzMap.put(name, out);
				return out;
			}
			return super.findClass(name);
		}
	}

	public static ThreadLocal<String> returnValue = new ThreadLocal<String>();
	public static ThreadLocal<ArrayList<Byte>> refValue = new ThreadLocal<ArrayList<Byte>>();
	public static ThreadLocal<ArrayList<ArrayList<Byte>>> header = new ThreadLocal<ArrayList<ArrayList<Byte>>>();
	public static ThreadLocal<Boolean> hasAccessFlags = new ThreadLocal<Boolean>();
	public static ThreadLocal<Boolean> hasStack = new ThreadLocal<Boolean>();
	public static ThreadLocal<Integer> stack = new ThreadLocal<Integer>();
	public static ThreadLocal<Boolean> hasLocals = new ThreadLocal<Boolean>();
	public static ThreadLocal<Integer> locals = new ThreadLocal<Integer>();
	public static ThreadLocal<Boolean> hasSuperClass = new ThreadLocal<Boolean>();
	public static ThreadLocal<Integer> superClass = new ThreadLocal<Integer>();
	public static HashMap<Integer, String> strVal = new HashMap<Integer, String>();
	public static ThreadLocal<Boolean> hasStackMapTable = new ThreadLocal<Boolean>();
	public static ThreadLocal<ArrayList<ArrayList<Byte>>> stackMapTable = new ThreadLocal<ArrayList<ArrayList<Byte>>>();
	public static ThreadLocal<Boolean> hasLocalVariableTable = new ThreadLocal<Boolean>();
	public static ThreadLocal<ArrayList<ArrayList<Byte>>> localVariableTable = new ThreadLocal<ArrayList<ArrayList<Byte>>>();


	public void init() {
		header.set(new ArrayList<ArrayList<Byte>>());
		hasAccessFlags.set(false);
		hasStack.set(false);
		hasLocals.set(false);
		hasSuperClass.set(false);
		stack.set(new Integer(0));
		hasStackMapTable.set(false);
		hasLocalVariableTable.set(false);
	}

	public class AsmClass {

		String name;
		AsmClass(String name) {this.name = name;}

		public byte[] create(ArrayList<Byte> ... args) {
			ArrayList<Byte>[] newArgs = (ArrayList<Byte>[]) new ArrayList[args.length + 1];
			System.arraycopy(args, 0, newArgs, 1, args.length);
			newArgs[0] = ref(classInfo(ref(utfStr(name))));
			return asmClass(newArgs);
		}
	}

	public class Method {

		ArrayList<Byte> flags;
		ArrayList<Byte> name;
		ArrayList<Byte> descriptor;

		Method(String declaration) {
			int flags = 0;
			String returnType;

			String first = declaration.substring(0, declaration.indexOf('('));
			StringTokenizer tokenizer = new StringTokenizer(first);
			//boolean hasReturnType = false;

			if (!declaration.matches("(?:public|private).*")) {
				flags |= public_;
			}

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();
				if (token.equals("public")) flags |= public_;
				else if (token.equals("private")) flags |= private_;
				else if (token.equals("protected")) flags |= protected_;
				else if (token.equals("static")) flags |= static_;
				else if (token.equals("final")) flags |= final_;
				else if (token.equals("volatile")) flags |= voatile;
				else if (token.equals("transient")) flags |= transient_;
				else if (token.equals("enum")) flags |= enum_;
				//else if (!hasReturnType) returnType = token;
				else name = ref(utfStr(token));
			}
			this.flags = flags(flags);
			descriptor = ref(utfStr(declaration.substring(declaration.indexOf('('), declaration.length())));
		}

		public ArrayList<Byte> create(ArrayList<Byte> ... args) {
			return method(
				this.flags
				, this.name
				, this.descriptor
				, code(args)
			);
		}
	}

	public byte[] asmClass(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(new Byte((byte) 0xCA));
		out.add(new Byte((byte) 0xFE));
		out.add(new Byte((byte) 0xBA));
		out.add(new Byte((byte) 0xBE));
		out.add(new Byte((byte) 0)); // minor version high
		out.add(new Byte((byte) 0)); // minor version low
		out.add(new Byte((byte) 0)); // major version high
		out.add(new Byte((byte) 0x34)); // major version low
		int i = 0;
		ArrayList<Byte> nameRef = args[i ++];
		int nameIndex = (nameRef.get(0).byteValue() << 8) | nameRef.get(1).byteValue();
		Object o = header.get();
		ArrayList<Byte> headIndex = header.get().get(nameIndex - 1);
		String name = strVal.get((headIndex.get(1) << 8) | headIndex.get(2));
		//ArrayList<Byte> sourceRef = ref(utfStr(strVal.get((headIndex.get(1) << 8) | headIndex.get(2)) + ".java"));
		//ArrayList<Byte> srcFileRef = ref(utfStr("SourceFile"));
		ArrayList<Byte> superRef = ref(classInfo(ref(utfStr("java/lang/Object"))));
		out.add(new Byte((byte) (((header.get().size() + 1) & 0xFF00) >>> 8)));// constant_pool_count high
		out.add(new Byte((byte) ((header.get().size() + 1) & 0xFF)));// constant_pool_count low
		for (ArrayList<Byte> constant : header.get()) out.addAll(constant);
		out.add(new Byte((byte) 0)); // access flags high
		out.add(new Byte((byte) (classPublic | classSuper))); // access flags low
		out.addAll(nameRef);                // class name
		if (hasSuperClass.get().booleanValue()) {
			out.addAll(args[i ++]);
		} else {
			out.addAll(superRef);
		}
		out.add(new Byte((byte) 0)); // interfaces_count high
		out.add(new Byte((byte) 0)); // interfaces_count low
		out.add(new Byte((byte) 0)); // fields_count high
		out.add(new Byte((byte) 0)); // fields_count low
		int remain = args.length - i;
		out.add((byte) ((remain & 0xFF00) >>> 8)); // methods_count high
		out.add((byte) (remain & 0xFF)); // methods_count low
		while (i < args.length) {
			out.addAll(args[i ++]);
		}
		out.add(new Byte((byte) 0)); // attributes_count high
		out.add(new Byte((byte) 0)); // attributes_count low
		//out.addAll(srcFileRef);
		//out.add(new Byte((byte) 0));
		//out.add(new Byte((byte) 0));
		//out.add(new Byte((byte) 0));
		//out.add(new Byte((byte) 2));
		//out.addAll(sourceRef);
		byte[] out2 = new byte[out.size()];
		for (int j = 0 ; j < out.size() ; j ++) {
			out2[j] = out.get(j).byteValue();
		}
		System.out.println("class: '" + name + "' compiled");
		clazzByteMap.put(name, out2);
		return out2;
	}

	public ArrayList<Byte> extendsClass(String clazz) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.addAll(ref(classInfo(ref(utfStr(clazz)))));
		hasSuperClass.set(true);
		return out;
	}

	public ArrayList<Byte> flags(int val) {
		hasAccessFlags.set(true);
		return intToRef(val);
	}

	public ArrayList<Byte> method(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		int i = 0;
		if (hasAccessFlags.get().booleanValue()) {
			out.addAll(args[i ++]); // access flags
			hasAccessFlags.set(false);
		} else {
			out.add(new Byte((byte) 0).byteValue());
			out.add(new Byte((byte) public_).byteValue());
		}
		out.addAll(args[i ++]); // name ref
		out.addAll(args[i ++]); // descriptor ref
		int remain = args.length - i;
		out.add((byte) ((remain & 0xFF00) >>> 8)); // length high
		out.add((byte) (remain & 0xFF)); // length low
		while (i < args.length) {
			out.addAll(args[i ++]);
		}
		String type = returnValue.get();
		return out;
	}

	public ArrayList<Byte> code(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.addAll(ref(utfStr("Code")));
		ArrayList<Byte> temp = new ArrayList<Byte>();
		for (ArrayList<Byte> arg : args) temp.addAll(arg);
		temp.add(last.get("return").byteValue());

		int attributeLength = temp.size() + 12;

		if (hasLocalVariableTable.get()) {
			for (ArrayList<Byte> entry : localVariableTable.get()) {
				attributeLength += entry.size();
			}
			attributeLength += 8;
		}
		if (hasStackMapTable.get()) {
			for (ArrayList<Byte> entry : stackMapTable.get()) {
				attributeLength += entry.size();
			}
			attributeLength += 8;
		}

		out.add((byte) ((attributeLength & 0xFF000000) >>> 24)); // attr length upper high
		out.add((byte) ((attributeLength & 0xFF0000) >>> 16)); // attr length upper low
		out.add((byte) ((attributeLength & 0xFF00) >>> 8)); // attr length high
		out.add((byte) (attributeLength & 0xFF)); // attr length low
		if (hasStack.get().booleanValue()) {
			out.add((byte) ((stack.get().intValue() & 0xFF00) >>> 8)); // max stack high
			out.add((byte) (stack.get().intValue() & 0xFF)); // max stack low
			hasStack.set(false);
		} else {
			out.add(new Byte((byte) 0));
			out.add(new Byte((byte) 0));
		}
		if (hasLocals.get().booleanValue()) {
			out.add((byte) ((locals.get().intValue() & 0xFF00) >>> 8)); // max locals high
			out.add((byte) (locals.get().intValue() & 0xFF)); // max locals low
			hasLocals.set(false);
		} else {
			out.add(new Byte((byte) 0));
			out.add(new Byte((byte) 1));
		}
		out.add((byte) ((temp.size() & 0xFF000000) >>> 24)); // code length upper high
		out.add((byte) ((temp.size() & 0xFF0000) >>> 16)); // code length upper low
		out.add((byte) ((temp.size() & 0xFF00) >>> 8)); // code length high
		out.add((byte) (temp.size() & 0xFF)); // code length low
		out.addAll(temp);
		out.add(new Byte((byte) 0)); // exception_table_length high
		out.add(new Byte((byte) 0)); // exception_table_length low
		out.add(new Byte((byte) 0)); // attributes_count high
		out.add(new Byte((byte) ((hasStackMapTable.get() ? 1 : 0) + (hasLocalVariableTable.get() ? 1 : 0)))); // attributes_count low
		if (hasLocalVariableTable.get()) out.addAll(localVariableTable());
		if (hasStackMapTable.get()) out.addAll(stackMapTable());
		return out;
	}

	private ArrayList<Byte> localVariableTable() {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.addAll(ref(utfStr("LocalVariableTable")));

		int local_variable_table_length = 0;
		ArrayList<Byte> entriesCollection = new ArrayList<Byte>();
		for (ArrayList<Byte> entry : localVariableTable.get()) {
			entriesCollection.addAll(entry);
			local_variable_table_length ++;
		}

		int attributeLength = entriesCollection.size() + 2; // num entries + entries length
		out.add((byte) ((attributeLength & 0xFF000000) >>> 24)); // attr length upper high
		out.add((byte) ((attributeLength & 0xFF0000) >>> 16)); // attr length upper low
		out.add((byte) ((attributeLength & 0xFF00) >>> 8)); // attr length high
		out.add((byte) (attributeLength & 0xFF)); // attr length low

		out.add((byte) ((local_variable_table_length & 0xFF00) >>> 8)); // local_variable_table_length high
		out.add((byte) (local_variable_table_length & 0xFF)); // local_variable_table_length low
		out.addAll(entriesCollection);
		return out;
	}

	public class LocalVariable {
		Byte[]  start_pc = new Byte[2],
				length = new Byte[2],
				name_index = new Byte[2],
				descriptor_index = new Byte[2],
				index = new Byte[2];
		public LocalVariable(int start, int length, ArrayList<Byte> name_index,
		                     ArrayList<Byte> descriptor_index, int index) {
			start_pc[0] = new Byte((byte) ((start & 0xff00) >>> 8));
			start_pc[1] = new Byte((byte) (start & 0xff));
			this.length[0] = new Byte((byte) ((length & 0xff00) >>> 8));
			this.length[1] = new Byte((byte) (length & 0xff));
			this.name_index[0] = name_index.get(0);
			this.name_index[1] = name_index.get(1);
			this.descriptor_index[0] = descriptor_index.get(0);
			this.descriptor_index[1] = descriptor_index.get(1);
			this.index[0] = new Byte((byte) ((index & 0xff00) >>> 8));
			this.index[1] = new Byte((byte) (index & 0xff));
		}
		public ArrayList<Byte> getBytes() {
			return new ArrayList<Byte>() {{
				addAll(Arrays.asList(start_pc));
				addAll(Arrays.asList(length));
				addAll(Arrays.asList(name_index));
				addAll(Arrays.asList(descriptor_index));
				addAll(Arrays.asList(index));
			}};
		}
	}

	private ArrayList<Byte> stackMapTable() {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.addAll(ref(utfStr("StackMapTable")));

		int num_entries = 0;
		ArrayList<Byte> entriesCollection = new ArrayList<Byte>();
		for (ArrayList<Byte> entry : stackMapTable.get()) {
			entriesCollection.addAll(entry);
			num_entries ++;
		}

		int attributeLength = entriesCollection.size() + 2; // num entries + entries length
		out.add((byte) ((attributeLength & 0xFF000000) >>> 24)); // attr length upper high
		out.add((byte) ((attributeLength & 0xFF0000) >>> 16)); // attr length upper low
		out.add((byte) ((attributeLength & 0xFF00) >>> 8)); // attr length high
		out.add((byte) (attributeLength & 0xFF)); // attr length low

		out.add((byte) ((num_entries & 0xFF00) >>> 8)); // num entries high
		out.add((byte) (num_entries & 0xFF)); // num entries low
		out.addAll(entriesCollection);
		return out;
	}

	public ArrayList<Byte> attributeSameFrameExtended_(int offset_delta) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add((byte) 251); // SAME_FRAME_EXTENDED
		out.add((byte) ((offset_delta & 0xff00) >>> 8));
		out.add((byte) (offset_delta & 0xff));
		return out;
	}

	public ArrayList<Byte> null_(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("aload_0").byteValue());
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> this_(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("aload_0").byteValue());
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> aload_(int index) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("aload").byteValue());
		out.add((byte) (index & 0xFF));
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> smartLoad_(String type, int index) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		if (type.startsWith("L") || type.startsWith("[")) {
			out.add(last.get("aload").byteValue());
		} else {
			out.add(last.get(type.toLowerCase() + "load").byteValue());
		}
		out.add((byte) (index & 0xFF));
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> astore_(int index) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("astore").byteValue());
		out.add((byte) (index & 0xFF));
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> smartStore_(String type, int index) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		if (type.startsWith("L") || type.startsWith("[")) {
			out.add(last.get("astore").byteValue());
		} else {
			out.add(last.get(type.toLowerCase() + "store").byteValue());
		}
		out.add((byte) (index & 0xFF));
		returnValue.set("A");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> super_(String clazz, String methodName, String methodType) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.addAll(invokeSpecial(clazz, methodName, methodType));
		return out;
	}

	public ArrayList<Byte> invokeSpecial(String clazz, String methodName, String methodType) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("invokespecial").byteValue());
		out.addAll(ref(methodRef(ref(classInfo(ref(utfStr(clazz)))), ref(nameAndType(methodName, methodType)))));
		addStack(-1);
		return out;
	}

	public ArrayList<Byte> string(String val) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ldc").byteValue());
		out.add(ref(string_(val)).get(1));
		returnValue.set("Ljava/lang/String;");
		addStack(1);
		return out;
	}

	public ArrayList<Byte> integer(int val) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ldc").byteValue());
		out.add(ref(integerConstant(val)).get(1));
		returnValue.set("I");
		addStack(1);
		return out;
	}

	private void addStack(int val) {
		if (!hasStack.get().booleanValue()) {
			hasStack.set(true);
			stack.set(1);
		}
		stack.set(stack.get().intValue() + val);
	}

	public ArrayList<Byte> print(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		String type = returnValue.get();
		out.addAll(callFieldRef("java/lang/System", ref(nameAndType("out", "Ljava/io/PrintStream;"))));
		out.addAll(args[0]);
		out.addAll(invokeVirtual(ref(methodRef(ref(classInfo(ref(utfStr("java/io/PrintStream")))), ref(nameAndType("print", "(" + type + ")V"))))));
		addStack(-1);
		return out;
	}

	public ArrayList<Byte> println(ArrayList<Byte> ... args) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		String type = returnValue.get();
		if (type.length() > 1) type = "Ljava/lang/Object;";
		out.addAll(callFieldRef("java/lang/System", ref(nameAndType("out", "Ljava/io/PrintStream;"))));
		out.addAll(args[0]);
		out.addAll(invokeVirtual(ref(methodRef(ref(classInfo(ref(utfStr("java/io/PrintStream")))), ref(nameAndType("println", "(" + type + ")V"))))));
		addStack(-1);
		return out;
	}

	public ArrayList<Byte> invokeVirtual(ArrayList<Byte> methodRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("invokevirtual").byteValue());
		out.addAll(methodRef);
		return out;
	}

	public ArrayList<Byte> loadConstant(ArrayList<Byte> ref) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ldc").byteValue());
		out.addAll(ref);
		addStack(1);
		return out;
	}

	public ArrayList<Byte> callFieldRef(String clazz, ArrayList<Byte> nameAndTypeRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("getstatic").byteValue());
		out.addAll(ref(fieldRef(ref(classInfo(ref(utfStr(clazz)))), nameAndTypeRef)));
		addStack(1);
		return out;
	}

	public ArrayList<Byte> ref(ArrayList<Byte> bytes) {
		return refValue.get();
	}

	public ArrayList<Byte> intToRef(int val) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add((byte) ((val & 0xFF00) >>> 8));
		out.add((byte) (val & 0xFF));
		return out;
	}

	private boolean headerHas(ArrayList<Byte> val) {
		outer:
		for (ArrayList<Byte> headerVal : header.get()) {
			for (int i = 0 ; i < val.size() && i < headerVal.size() ; i ++) {
				if (val.get(i).byteValue() != headerVal.get(i).byteValue()) {
					continue outer;
				}
			}
			return true;
		}
		return false;
	}

	private void ensureHeaderConst(ArrayList<Byte> val) {
		outer:
		for (int i = 0 ; i < header.get().size() ; i ++) {
			ArrayList<Byte> headerVal = header.get().get(i);
			for (int j = 0 ; j < val.size() && j < headerVal.size() ; j ++) {
				if (val.get(j).byteValue() != headerVal.get(j).byteValue()) {
					continue outer;
				}
			}
			refValue.set(intToRef(i + 1));
			return;
		}
		header.get().add(val);
		refValue.set(intToRef(header.get().size()));
	}

	public ArrayList<Byte> classInfo(ArrayList<Byte> utfRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(class_);
		out.addAll(utfRef);
		ensureHeaderConst(out);
		return out;
	}

	public ArrayList<Byte> methodRef(ArrayList<Byte> clazz, ArrayList<Byte> nameAndTypeRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(methodRef);
		//classInfo(clazz);
		out.addAll(clazz);
		out.addAll(nameAndTypeRef);
		ensureHeaderConst(out);
		return out;
	}

	public ArrayList<Byte> fieldRef(ArrayList<Byte> clazzRef, ArrayList<Byte> nameAndTypeRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(fieldRef);
		//classInfo(clazzRef);
		out.addAll(clazzRef);
		out.addAll(nameAndTypeRef);
		ensureHeaderConst(out);
		return out;
	}

	public ArrayList<Byte> lessThan_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("iflt").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> lessThanEqual_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ifle").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> greaterThan_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ifgt").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> ifCompareGreaterThan_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("if_icmpgt").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> ifCompareLessThan_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("if_icmplt").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> goto_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("goto").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> iinc_(int varIndex, int amount) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("iinc").byteValue());
		out.add((byte) varIndex);
		out.add((byte) (amount & 0xff));
		return out;
	}

	public ArrayList<Byte> greaterThanEqual_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("ifge").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}
	public ArrayList<Byte> subtract_() {
		ArrayList<Byte> out = new ArrayList<Byte>();
		String type = returnValue.get();
		if (type.length() > 1) throw new TypeMismatchException("Can't subtract non number types");
		out.add(last.get(type.toLowerCase() + "sub").byteValue());
		addStack(-1);
		return out;
	}

	public ArrayList<Byte> add_() {
		ArrayList<Byte> out = new ArrayList<Byte>();
		String type = returnValue.get();
		if (type.length() > 1) throw new TypeMismatchException("Can't add non number types");
		out.add(last.get(type.toLowerCase() + "add").byteValue());
		addStack(-1);
		return out;
	}

	public ArrayList<Byte> jsr_(int branchOffset) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(last.get("jsr").byteValue());
		out.add((byte) ((branchOffset & 0xFF00) >>> 8));
		out.add((byte) (branchOffset & 0xFF));
		return out;
	}

	public ArrayList<Byte> string_(String val) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(string_);
		out.addAll(ref(utfStr(val)));
		ensureHeaderConst(out);
		return out;
	}

	private ArrayList<Byte> integerConstant(int val) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(integer);
		out.add((byte) ((val & 0xFF000000) >>> 24)); // val upper high
		out.add((byte) ((val & 0xFF0000) >>> 16)); // val upper low
		out.add((byte) ((val & 0xFF00) >>> 8)); // val length high
		out.add((byte) (val & 0xFF)); // val length low
		ensureHeaderConst(out);
		return out;
	}

	public ArrayList<Byte> nameAndType(String name, String type) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		out.add(nameAndType);
		out.addAll(ref(utfStr(name)));
		ArrayList<Byte> typeRef = ref(utfStr(type));
		//classInfo(typeRef);
		out.addAll(typeRef);
		ensureHeaderConst(out);
		return out;
	}

	public ArrayList<Byte> utfStr(String strRef) {
		ArrayList<Byte> out = new ArrayList<Byte>();
		int len = strRef.length();
		out.add(utf8);
		out.add((byte) ((len & 0xFF00) >>> 8));
		out.add((byte) (len & 0xFF));
		for (byte b : strRef.getBytes()) out.add(b);
		ensureHeaderConst(out);
		int refVal = (refValue.get().get(0) << 8) | refValue.get().get(1);
		strVal.put(refVal, strRef);
		return out;
	}
}
