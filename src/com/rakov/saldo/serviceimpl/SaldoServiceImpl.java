package com.rakov.saldo.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.rakov.saldo.dao.LemgramDAO;
import com.rakov.saldo.daoimpl.LemgramDaoImpl;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.model.ServiceSupportModel;
import com.rakov.saldo.service.SaldoService;

public class SaldoServiceImpl implements SaldoService {
	private static int min_seg = 3;
	private static int max_parts = 4;

	private HashMap<String, String> prefix_c = new HashMap<String, String>();
	private HashMap<String, String> prefix_ci = new HashMap<String, String>();

	private HashMap<String, String> prefix_cm = new HashMap<String, String>();
	private HashMap<String, HashMap<String, String>> suffix = new HashMap<String, HashMap<String, String>>();
	private List<String> prefix_searched = new ArrayList<String>();
	private List<String> suffix_searched = new ArrayList<String>();
	private LemgramDAO lemDAO = new LemgramDaoImpl();
	private static HashMap<String, String> posMap = new HashMap<String, String>();
	static {
		posMap.put("nn", "nn");
		posMap.put("vb", "vb");
		posMap.put("av", "av");
		posMap.put("ab", "ab");
	}

	private Pattern reWord = Pattern
			.compile("([a-zA-ZåäöéüÅÄÖÉÜ0-9]+-)*[a-zA-ZåäöéüÅÄÖÉÜ]+(:[a-zA-Z]+)?$");
	private Pattern reDash = Pattern.compile("-+");

	@Override
	public String[] split(String word, String pos) {
		boolean isNotWord = reWord.matcher(word).matches();
		String parts[] = reDash.split(word);
		if (parts.length >= max_parts) {
			return null;
		} else {
			if (parts.length >= 2) {
				if (this.hasSuffix(parts[0], pos)) {
					for (int j = 0; j < parts.length; j++) {
						if (parts[j] == "") {
							return null;
						}
					}
					return parts;
				}
				if (!isNotWord) {

					return null;
				}
				List<String> segs = new ArrayList<String>();
				segs = this.analyze(word, pos, false);
				if (segs == null) {
					return null;
				}
				if (segs.isEmpty()) {
					return null;
				} else {
					for (int i = 0; i < parts.length && i < segs.size(); i++) {
						parts[i] += segs.get(i);
					}
					return parts;
				}

			}
			if (parts.length == 1) {
				if (!isNotWord) {
					return null;
				}
				List<String> segs1 = this.analyze(word, pos, false);
				String res[] = new String[segs1.size()];
				for (int i = 0; i < segs1.size(); i++) {
					res[i] = segs1.get(i);
				}
				return res;
			} else {
				return null;
			}
		}

	}


	private boolean hasPrefix(String prefix, boolean initial) {

		for (int i = 0; i < prefix_searched.size(); i++) {
			if (prefix.equals(prefix_searched.get(i))) {
				if (prefix_c.containsKey(prefix)) {
					return true;
				}
				if (initial && prefix_ci.containsKey(prefix)) {
					return true;
				}
				if (!initial && prefix_cm.containsKey(prefix)) {
					return true;
				}
			}
		}
		boolean prefixSerched = false;
		List<Lemgram> lems = lemDAO.getLemgramByForm(prefix);
		for (int j = 0; j < lems.size(); j++) {
			String[] tags = lems.get(j).getMsd().split(" ");
			for (int i = 0; i < tags.length; i++) {
				if (tags[i].equals("c")) {
					prefixSerched = true;
					prefix_c.put(prefix, lems.get(j).getLemgram());
				}
				if (tags[i].equals("ci")) {
					prefix_ci.put(prefix, lems.get(j).getLemgram());
					if (initial) {
						prefixSerched = true;

					}
				}
				if (tags[i].equals("cm")) {
					prefix_cm.put(prefix, lems.get(j).getLemgram());
					if (!initial) {
						prefixSerched = true;

					}
				}
			}

		}
		prefix_searched.add(prefix);
		return prefixSerched;

	}

	
	private boolean hasSuffix(String suffix, String pos) {
		for (int i = 0; i < suffix_searched.size(); i++) {
			if (suffix.equals(suffix_searched.get(i))) {
				if (this.suffix.containsKey(pos)) {
					return true;
				}
			}
		}
		boolean prefixSerched = false;
		List<Lemgram> lems = lemDAO.getLemgramByGf(suffix);
		for (int j = 0; j < lems.size(); j++) {
			String suc_pos = posMap.get(lems.get(j).getPos().toLowerCase());
			if (suc_pos != null) {
				HashMap<String, String> temp = new HashMap<String, String>();
				if (this.suffix.containsKey(suc_pos)) {
					temp = this.suffix.get(suc_pos);
				}
				if (!temp.isEmpty()) {
					temp.put(suffix, lems.get(j).getLemgram());
					this.suffix.put(suc_pos, temp);
				} else {
					HashMap<String, String> temp1 = new HashMap<String, String>();
					temp1.put(suffix, lems.get(j).getLemgram());
					this.suffix.put(suc_pos, temp1);
				}
				if (pos.equals(suc_pos)) {
					prefixSerched = true;
				}
			}

		}
		this.suffix_searched.add(suffix);

		return prefixSerched;

	}

	private List<String> analyze(String word, String pos, boolean initial) {

		HashMap<Integer, ArrayList<ServiceSupportModel>> endsAt = new HashMap<Integer, ArrayList<ServiceSupportModel>>();
		int[] lenAt = new int[word.length() + 1];
		for (int i = min_seg; i < word.length() - min_seg + 1; i++) {
			String seg = word.substring(0, i);
			if (hasPrefix(seg, initial)) {
				ServiceSupportModel mod = new ServiceSupportModel(0, 0, "");
				mod.setI(0);
				mod.setJ(i);
				mod.setSeg(seg);
				if (endsAt.containsKey(i)) {
					ArrayList<ServiceSupportModel> temp = endsAt.get(i);
					temp.add(mod);
					endsAt.put(i, temp);
				} else {
					ArrayList<ServiceSupportModel> temp = new ArrayList<ServiceSupportModel>();
					temp.add(mod);
					endsAt.put(i, temp);
				}

				lenAt[i] = 1;
			}
			for (int j = min_seg; j < i - min_seg + 1; j++) {
				if (endsAt.containsKey(j)) {
					seg = word.substring(j, i);
					if (hasPrefix(seg, false)) {
						ServiceSupportModel mod1 = new ServiceSupportModel(0,
								0, "");
						mod1.setI(j);
						mod1.setJ(i);
						mod1.setSeg(seg);
						if (endsAt.containsKey(i)) {
							ArrayList<ServiceSupportModel> temp = endsAt.get(i);
							temp.add(mod1);
							endsAt.put(i, temp);
						} else {
							ArrayList<ServiceSupportModel> temp = new ArrayList<ServiceSupportModel>();
							temp.add(mod1);
							endsAt.put(i, temp);
						}
						if (lenAt[i] == 0 || lenAt[i] > 1 + lenAt[j]) {
							lenAt[i] = 1 + lenAt[j];
						}
					}
					if (word.charAt(j - 2) == word.charAt(j - 1)) {
						seg = word.charAt(j - 1) + seg;
						if (hasPrefix(seg, false)) {
							ServiceSupportModel mod1 = new ServiceSupportModel(
									0, 0, "");
							mod1.setI(j);
							mod1.setJ(i);
							mod1.setSeg(seg);
							if (endsAt.containsKey(i)) {
								ArrayList<ServiceSupportModel> temp = endsAt
										.get(i);
								temp.add(mod1);
								endsAt.put(i, temp);
							} else {
								ArrayList<ServiceSupportModel> temp = new ArrayList<ServiceSupportModel>();
								temp.add(mod1);
								endsAt.put(i, temp);
							}
							if (lenAt[i] == 0 || lenAt[i] > 1 + lenAt[j]) {
								lenAt[i] = 1 + lenAt[j];
							}
						}
					}

				}
			}

		}
		int j = word.length();

		for (int i = min_seg; i < j - min_seg + 1; i++) {
			if (endsAt.containsKey(i)) {
				String seg = word.substring(i, j);
				if (hasSuffix(seg, pos)) {
					ServiceSupportModel mod1 = new ServiceSupportModel(0, 0, "");
					mod1.setI(i);
					mod1.setJ(j);
					mod1.setSeg(seg);
					if (endsAt.containsKey(j)) {
						ArrayList<ServiceSupportModel> temp = endsAt.get(j);
						temp.add(mod1);
						endsAt.put(j, temp);
					} else {
						ArrayList<ServiceSupportModel> temp = new ArrayList<ServiceSupportModel>();
						temp.add(mod1);
						endsAt.put(j, temp);
					}
					if (lenAt[j] == 0 || lenAt[j] > 1 + lenAt[i]) {
						lenAt[j] = 1 + lenAt[i];
					}
				}
				if (word.charAt(i - 2) == word.charAt(i - 1)) {
					seg = word.charAt(i - 1) + seg;
					if (hasSuffix(seg, pos)) {
						ServiceSupportModel mod2 = new ServiceSupportModel(0,
								0, "");
						mod2.setI(i);
						mod2.setJ(j);
						mod2.setSeg(seg);
						if (endsAt.containsKey(j)) {
							ArrayList<ServiceSupportModel> temp = endsAt.get(j);
							temp.add(mod2);
							endsAt.put(j, temp);
						} else {
							ArrayList<ServiceSupportModel> temp = new ArrayList<ServiceSupportModel>();
							temp.add(mod2);
							endsAt.put(j, temp);
						}
						if (lenAt[j] == 0 || lenAt[j] > 1 + lenAt[i]) {
							lenAt[j] = 1 + lenAt[i];
						}
					}
				}

			}
		}
		if (!endsAt.containsKey(j)) {
			return null;
		}
		if (lenAt[j] > 4) {
			return null;
		}
		List<String> strBuf = new ArrayList<String>();
		SaldoServiceImpl.combine(strBuf, j, endsAt, lenAt[j], "");
		return strBuf;

	}

	private static void combine(List<String> strBuf, int j,
			HashMap<Integer, ArrayList<ServiceSupportModel>> endsAt1,
			int lenAtJ, String history) {
		if (lenAtJ <= 0) {
			return;
		}
		for (int i = 0; i < endsAt1.get(j).size(); i++) {
			if (endsAt1.get(j).get(i).getI() == 0) {

				strBuf.add(endsAt1.get(j).get(i).getSeg());
				strBuf.add(history);

			} else {
				combine(strBuf, endsAt1.get(j).get(i).getI(), endsAt1,
						lenAtJ - 1, endsAt1.get(j).get(i).getSeg() + history);
			}

		}

	}

	@Override
	public HashMap<Boolean, HashMap<String, ArrayList<Lemgram>>> isSemanticCompound(String word, String[] segs, String pos) {
	
		boolean result = hasSuffix(word, pos);
		List<Lemgram> tempWordAncestor = lemDAO.getLemgramByForm(word);
		HashMap<String, ArrayList<Lemgram>> analyzeResult = new HashMap<String, ArrayList<Lemgram>>();

		for (int i = 0; i < tempWordAncestor.size(); i++) {
			if (analyzeResult.containsKey("wordAncestors")) {
				ArrayList<Lemgram> t = analyzeResult.get("wordAncestors");
				ArrayList<Lemgram> temp = lemDAO.getSense(tempWordAncestor.get(i).getSense());
				for (int j = 0; j < temp.size(); j++) {
					if (!t.contains(temp.get(j))) {
						t.add(temp.get(j));
					}
				}
				analyzeResult.remove("wordAncestors");
				analyzeResult.put("wordAncestors", t);

			} else {
				ArrayList<Lemgram> t = new ArrayList<Lemgram>();
				ArrayList<Lemgram> temp = lemDAO.getSense(tempWordAncestor.get(i).getSense());
				for (int j = 0; j < temp.size(); j++) {
					if (!t.contains(temp.get(j))) {
						t.add(temp.get(j));
						
					}

				}
				analyzeResult.put("wordAncestors", t);
			}
		}
		ArrayList<Lemgram> segAncestors = new ArrayList<Lemgram>();
		for (int j = 0; j < segs.length; j++) {
			List<Lemgram> tempSegAncestor = lemDAO.getLemgramByForm(segs[j]);
			for (int i = 0; i < tempSegAncestor.size(); i++) {
				ArrayList<Lemgram> temp = lemDAO.getSense(tempSegAncestor
						.get(i).getSense());
				for (int k = 0; k < temp.size(); k++) {
					if (!segAncestors.contains(temp.get(k))) {
						segAncestors.add(temp.get(k));
					}
				}
			}
		}
		analyzeResult.put("segAncestors", segAncestors);
		HashMap<Boolean ,HashMap<String, ArrayList<Lemgram>>> fResult=new HashMap<Boolean,HashMap<String, ArrayList<Lemgram>>>();
		fResult.put(result, analyzeResult);
		return fResult;
	}

}
