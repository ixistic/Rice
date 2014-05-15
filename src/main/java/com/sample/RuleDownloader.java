package com.sample;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RuleDownloader {
	private DatabaseConnector db;
	private String ruleFileName;
	private BufferedWriter out;
	private int ruleCounter = 0;
	private static final String HEADER = "import com.sample.Drools.Message;\n";

	public RuleDownloader(DatabaseConnector db, String ruleFilename) {
		db.establishConnection();
		this.db = db;
		this.ruleFileName = ruleFilename;
	}

	public void download() {
		ruleCounter = 0;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(ruleFileName), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			out.write(HEADER);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String sql = "SELECT * FROM rules";
		ResultSet rules = db.executeQuery(sql);
		try {
			while (rules.next()) {
				String _if = rules.getString("if");
				String _then = rules.getString("then");
				String _tag = rules.getString("tag");
				writeToFile(_if, _then, _tag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rules.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Downloaded successfully!");
	}

	private void writeToFile(String _if, String _then, String _tag) {
		ruleCounter += 1;
		try {
			out.write("rule \"" + ruleCounter + "\"\n\twhen\n\t\tm:Message(");
					
			String[] conditions = _if.split(",");
			int countCondition = 0;
			for (String f : conditions) {
				countCondition++;
				out.write("message==\"" + f.trim() + "\"");
				if (countCondition != conditions.length) {
					out.write(" || ");
				}
			}

			out.write(");\n\tthen\n");
			
			for (String t : _then.split(",")) {
				out.write("\t\tm.addResult(\"" + t.trim() + "\",\"" + _tag
						+ "\");\n");
			}
			out.write("end\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] agrs) {
		String username = "root";
		String password = "";
		String databaseName = "kekm";
		String host = "localhost";
		String port = "3306";
		String connectionString = "jdbc:mysql://" + host + ":" + port + "/"
				+ databaseName + "?useUnicode=true&characterEncoding=UTF-8";
		DatabaseConnector db = new DatabaseConnector(connectionString,
				username, password);
		RuleDownloader rd = new RuleDownloader(db, "xrules.drl");
		rd.download();
	}
}
