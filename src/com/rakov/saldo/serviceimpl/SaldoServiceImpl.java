package com.rakov.saldo.serviceimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.rakov.saldo.dao.LemgramDAO;
import com.rakov.saldo.daoimpl.LemgramDaoImpl;
import com.rakov.saldo.model.Lemgram;
import com.rakov.saldo.model.SemanticCompoundSupport;
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

	private Pattern reWord = Pattern.compile(
			"([a-zA-ZåäöéüÅÄÖÉÜ0-9]+-)*[a-zA-ZåäöéüÅÄÖÉÜ]+(:[a-zA-Z]+)?$",
			Pattern.UNICODE_CHARACTER_CLASS);
	private Pattern reDash = Pattern.compile("-+");
/*
 * This method returns null if cann't split this word or
 *  HashMap<Integer, String[]> where int is number of splits and string [] is split
 * @see com.rakov.saldo.service.SaldoService#split(java.lang.String, java.lang.String)
 */
	@Override
	public HashMap<Integer, String[]> split(String word, String pos) {
		boolean isNotWord = reWord.matcher(word).matches();
		String parts[] = reDash.split(word);
		HashMap<Integer, String[]> res = new HashMap<Integer, String[]>();
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

					int num = res.keySet().size();
					res.put(num, parts);
					return res;
				}
				if (!isNotWord) {

					return null;
				}
				// List<String> segs = new ArrayList<String>();
				HashMap<Integer, String[]> segs = this
						.analyze(word, pos, false);
				if (segs == null) {
					return null;
				}
				if (segs.isEmpty()) {
					return null;
				} else {
					for (int i = 0; i < parts.length && i < segs.size(); i++) {
						parts[i] += segs.get(i);
					}
					int num = res.keySet().size();
					res.put(num, parts);
					return res;
				}

			}
			if (parts.length == 1) {
				if (!isNotWord) {
					return null;
				}

				return this.analyze(word, pos, false);
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

	private HashMap<Integer, String[]> analyze(String word, String pos,
			boolean initial) {

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
		HashMap<Integer, String[]> strBuf = new HashMap<Integer, String[]>();
		SaldoServiceImpl.combine(strBuf, j, endsAt, lenAt[j], "");
		return strBuf;

	}

	private static void combine(HashMap<Integer, String[]> strBuf, int j,
			HashMap<Integer, ArrayList<ServiceSupportModel>> endsAt1,
			int lenAtJ, String history) {
		if (lenAtJ <= 0) {
			return;
		}
		for (int i = 0; i < endsAt1.get(j).size(); i++) {
			if (endsAt1.get(j).get(i).getI() == 0) {
				String[] temp = new String[2];
				temp[0] = (endsAt1.get(j).get(i).getSeg());
				temp[1] = (history);
				int num = strBuf.keySet().size();
				strBuf.put(num, temp);

			} else {
				combine(strBuf, endsAt1.get(j).get(i).getI(), endsAt1,
						lenAtJ - 1, endsAt1.get(j).get(i).getSeg() + history);
			}

		}

	}
/*This method returns null if cann't find full word in dictionary or boolean which shoes the quality of splits
 * */
	@Override
	public List<SemanticCompoundSupport> isSemanticCompound(
			HashMap<Integer, String[]> sRes, String pos, String word) {
		List<SemanticCompoundSupport> result = new ArrayList<SemanticCompoundSupport>();

		if (!hasSuffix(word, pos)) {
			return null;
		}
		for (int i = 0; i < sRes.size(); i++) {
			SemanticCompoundSupport sup = new SemanticCompoundSupport();
			sup.setParts(sRes.get(i));

			List<Lemgram> tempWordAncestors = lemDAO.getLemgramByForm(word);
			HashSet<Lemgram> wordAncestors = this
					.getAncestors(tempWordAncestors);
			List<Lemgram> tempSegAncestors = new ArrayList<Lemgram>();
			for (int j = 0; j < sup.getParts().length; j++) {
				tempSegAncestors
						.addAll(lemDAO.getLemgramByForm(sup.getParts()[j]));
			}
			HashSet<Lemgram> segAncestors = this.getAncestors(tempSegAncestors);
			boolean resultq = false;
			Iterator<Lemgram> iterWA = wordAncestors.iterator();
			// System.out.println("word ancestors");
			while (iterWA.hasNext()) {
				Lemgram lemwa = iterWA.next();
				// System.out.println(lemwa);
				if (!resultq) {

					if (segAncestors.contains(lemwa)) {
						resultq = true;
					}
				}
			}
			/*
			 * Iterator<Lemgram> iterSA=segAncestors.iterator();
			 * System.out.println("segs ancestors"); while (iterSA.hasNext()){
			 * System.out.println(iterSA.next().toString()); }
			 */
			sup.setFlagIsComp(resultq);

			result.add(sup);

		}

		return result;

	}

	private HashSet<Lemgram> getAncestors(List<Lemgram> lem) {
		HashSet<Lemgram> senses = new HashSet<Lemgram>();
		for (int i = 0; i < lem.size(); i++) {
			senses.addAll(lemDAO.getLemgramByName(lem.get(i).getLemgram()));

		}

		HashSet<Lemgram> ancestors = new HashSet<Lemgram>();
		for (int i = 0; i < lem.size(); i++) {
			ancestors.addAll(lemDAO.getSense(lem.get(i).getSense()));

		}
		HashSet<Lemgram> tempSenses = new HashSet<Lemgram>();
		HashSet<Lemgram> tempAnsestors = new HashSet<Lemgram>();
		tempSenses.addAll(senses);
		tempAnsestors.addAll(ancestors);
		Iterator<Lemgram> iterSen = tempSenses.iterator();
		Iterator<Lemgram> iterAnc = tempAnsestors.iterator();
		while (iterSen.hasNext()) {
			senses.addAll(lemDAO.getSense(iterSen.next().getFather()));
		}
		while (iterAnc.hasNext()) {
			ancestors.addAll(lemDAO.getSense(iterAnc.next().getFather()));
		}
		HashSet<Lemgram> result = new HashSet<Lemgram>();
		result.addAll(ancestors);
		result.addAll(senses);

		return result;

	}

}
