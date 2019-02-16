//
// Copyright Martin Cenek <drcenek@gmail.com> 2016-2019
//
// All source code is released under the terms of the MIT License.
// See LICENSE for more information.
// Contributions from:
// Eric Pak, Levi Oyster, Boyd Ching, Rowan Bulkow, Neal Logan, Mackenzie Bartlett
//
//package threejsFileGen;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GexfReader {
	private static boolean logging = Generator.logging;
	Set<String> dateStrings = new HashSet<String>();
	Set<Integer> dateInts = new HashSet<Integer>();

	public static String edgeToken = "<edge";
	public static String nodeToken = "<node";

	public Layer createLayerFromFile(Path filePath, int fileCounter) {
		Layer layer;

		if (logging)
			System.out.println("Path: " + filePath.toString());

		String dateString = findDate(filePath.toString());
		int dateInt = convertDateToInt(0, dateString);
		if (dateInt == 0)
			dateInt = 20190101;
		if (logging)
			System.out.println("Date string: " + dateString + " int: " + dateInt);
		if (!dateStrings.add(dateString) || !dateInts.add(dateInt)) {
			System.err.println("Error - Duplicate Date string: " + dateString + " int: " + dateInt);
			System.exit(1);
		}

		layer = readFile(filePath.toString(), dateInt, fileCounter);
		return layer;
	}

	public Layer readFile(String path, int dateIn, int fileCounter) {
		Layer layer = new Layer(dateIn);
		try {
			Scanner file = new Scanner(new File(path));
			while (file.hasNext()) {
				String token = file.next();
				if (token.equalsIgnoreCase(edgeToken)) {
					layer.addEdge(readEdge(file));
				} else if (token.equalsIgnoreCase(nodeToken)) {
					layer = readNode(layer, file, fileCounter);
				}
			}
		} catch (IOException e) {
			System.err.println("Error - Trouble reading file to layer, path: " + path);
			System.err.println(e.getMessage());
			System.exit(1);
		}
		return layer;
	}

	private Layer readNode(Layer layer, Scanner file, int fileCounter) throws IOException {
		String token = "";
		Node node = new Node();
		int communityId = 0, z = 0;

		do {
			token = file.next();
			if (token.startsWith("id=")) {
				node.setId(token.substring(4, findSecondQuote(4, token)));
			} else if (token.startsWith("label=")) {
				node.setLabel(token.substring(7, findSecondQuote(7, token)));
			} else if (token.equalsIgnoreCase("for=\"modularity_class\"")) {
				token = file.next();
				communityId = Integer.parseInt(token.substring(7, findSecondQuote(7, token)));
			} else if (token.equalsIgnoreCase("<viz:size")) {
				token = file.next();
				node.setSize(Double.parseDouble(token.substring(7, findSecondQuote(7, token))));
			} else if (token.startsWith("start=")) {
				z = convertDateToInt(7, token);
			} else if (token.startsWith("x=")) {
				node.setX(Double.parseDouble(token.substring(3, findSecondQuote(3, token))));
			} else if (token.startsWith("y=")) {
				node.setY(Double.parseDouble(token.substring(3, findSecondQuote(3, token))));
			} else if (token.equalsIgnoreCase("<viz:color")) {
				int r, g, b;
				token = file.next();
				r = Integer.parseInt(token.substring(3, findSecondQuote(3, token)));
				token = file.next();
				g = Integer.parseInt(token.substring(3, findSecondQuote(3, token)));
				token = file.next();
				b = Integer.parseInt(token.substring(3, findSecondQuote(3, token)));
				node.setColor(new NodeColor(r, g, b));
			}
		} while (!token.equalsIgnoreCase("</node>"));

		if (layer.checkCommunityIdExists(communityId)) {
			layer.getCommunity(communityId).addNode(node);
		} else {
			layer.addCommunity(new Community(communityId), communityId);
			layer.getCommunity(communityId).addNode(node);
			layer.getCommunity(communityId).setColor(node.getColor());
		}
		return layer;
	}

	private Edge readEdge(Scanner file) throws IOException {
		String token;
		Edge edge = new Edge();

		do {
			token = file.next();

			if (token.startsWith("source=")) {
				edge.setSource(token.substring(8, findSecondQuote(8, token)));
			} else if (token.startsWith("target=")) {
				edge.setTarget(token.substring(8, findSecondQuote(8, token)));
			} else if (token.startsWith("for=\"weight")) {
				token = file.next();
				if (token.startsWith("value=\"")) {
					edge.setWeight(Double.parseDouble(token.substring(7, findSecondQuote(7, token))));
				}
			} else if (token.startsWith("start=")) {
				edge.setStart(convertDateToInt(7, token));
				if (token.endsWith(">")) {
					edge.setEnd(convertDateToInt(7, token));
				}
			} else if (token.startsWith("endopen=")) {
				edge.setEnd(convertDateToInt(9, token));
			}
		} while (!token.endsWith("</edge>"));
		return edge;
	}

	// Finds the second double quote
	private int findSecondQuote(int i, String s) {
		int secondQuoteLocation = s.length();
		for (int j = i; j < s.length(); j++) {
			if (s.charAt(j) == '"') {
				secondQuoteLocation = j;
			}
		}
		return secondQuoteLocation;
	}

	// Finds the date in a string with yyyy-mm-dd format
	public String findDate(String s) {
		Pattern p = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		Matcher m = p.matcher(s);
		if (m.find()) {
			return m.group(0);
		}
		return "Date Not Found";
	}

	// Converts a yyyy-mm-dd format into integer format
	public int convertDateToInt(int firstQuotePosition, String token) {
		int date;
		String unparsedDate;
		int year;
		int month;
		int day;

		unparsedDate = token.substring(firstQuotePosition, findSecondQuote(firstQuotePosition, token));
		year = Integer.parseInt(unparsedDate.substring(0, 4));
		month = Integer.parseInt(unparsedDate.substring(5, 7));
		day = Integer.parseInt(unparsedDate.substring(8, 10));

		// Not adjusted for Leap year.
		year = year * 365;
		int daysInMonthsSoFar = 0;
		int[] daysInEachMonth = { 31, // January
				28, // February
				31, // March
				30, // April
				31, // May
				30, // June
				31, // July
				31, // August
				30, // September
				31, // October
				30, // November
				31 // December
		};
		for (int i = 0; i < month - 1; i++) {
			// if month=1, we don't want daysInMonthsSoFar=0
			// and if month=12, we only want up to November added in
			// i.e. we never actually use December
			daysInMonthsSoFar += daysInEachMonth[i];
		}

		date = day + daysInMonthsSoFar + year;
		return date;
	}
}
