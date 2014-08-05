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
/*
 * 
      RE_WORD = re.compile(
        ur'([a-zA-ZåäöéüÅÄÖÉÜ0-9]+-)*[a-zA-ZåäöéüÅÄÖÉÜ]+(:[a-zA-Z]+)?$')
    RE_DASH = re.compile(ur'-+')


    def split(self, word, pos):
        is_not_word = Splitter.RE_WORD.match(word) is None
        parts = Splitter.RE_DASH.split(word)
        if len(parts) >= self.max_parts: return None
        elif len(parts) >= 2:
            if self._has_suffix(parts[-1], pos):
                if u"" in parts: return None
                return [parts]
            if is_not_word: return None
            segs = self.analyze(parts[-1], pos, False)
            if segs is None: return [parts]
            else: return [parts[:-1] + seg for seg in segs]
        elif len(parts) == 1:
            if is_not_word: return None
            return self.analyze(word, pos)
        else: return None
*/
	 Pattern reWord = Pattern.compile("([a-zA-ZåäöéüÅÄÖÉÜ0-9]+-)*[a-zA-ZåäöéüÅÄÖÉÜ]+(:[a-zA-Z]+)?$");
	 Pattern reDash=Pattern.compile("-+");
	@Override
	public String[]  split(String word, String pos) {
		boolean isNotWord=reWord.matcher(word).matches();
		String parts []= reDash.split(word);
		if(parts.length>=max_parts)
		{
			return null;
		}
		else{
			if(parts.length>=2){
				if(this.hasSuffix(parts[0], pos))
				{//TODO: ask   if u"" in parts: return None
					return parts;
				}
				if(isNotWord){
					return null;
				}
				List<String> segs= new ArrayList<String>();
				segs=this.analyze(word, pos, false);
				if(segs==null)
				{
					return null;
				}
				if(segs.isEmpty())
				{
					return null;
				}
				//TODO: ask  else: return [parts[:-1] + seg for seg in segs]
			}
			if(parts.length==1)
			{
				if(isNotWord){
					return null;
				}
				return (String[]) this.analyze(word, pos, false).toArray();
			}
			else{
				return null;
			}
		}
		//return null;
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
			if (lems.get(j).getMsd().equals("c")) {
				prefixSerched = true;
				prefix_c.put(prefix, lems.get(j).getLemgram());
			}
			if (lems.get(j).getMsd().equals("ci")) {
				prefix_ci.put(prefix, lems.get(j).getLemgram());
				if (initial) {
					prefixSerched = true;

				}
			}
			if (lems.get(j).getMsd().equals("cm")) {
				prefix_cm.put(prefix, lems.get(j).getLemgram());
				if (!initial) {
					prefixSerched = true;

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
			String suc_pos = lems.get(j).getPos().toLowerCase();
			if (this.suffix.containsKey(suc_pos)) {
				HashMap<String, String> temp = this.suffix.get(suc_pos);
				if (!temp.isEmpty()) {
					temp.put(suffix, lems.get(j).getLemgram());
					this.suffix.put(suc_pos, temp);
				} else {
					HashMap<String, String> temp1 = new HashMap<String, String>();
					temp1.put(suffix, lems.get(j).getLemgram());
					this.suffix.put(suc_pos, temp1);
				}
				if(pos.equals(suc_pos))
				{
					prefixSerched=true;
				}
			}

		}
		this.suffix_searched.add(suffix);

		return prefixSerched;

	}
	/*
	 *   def analyze(self, word, pos, initial=True):
        ends_at = [[] for _ in range(len(word)+1)]
        len_at = [None] * (len(word)+1)
        len_at[0] = 0
        # Try to find candidates for the initial and middle segments
        for j in range(self.min_seg, len(word)-self.min_seg+1):
            # First try the initial segment (the word itself is
            # compound-initial, as indicated by _initial_, otherwise this is
            # a middle segment)
            seg = word[:j]
            if self._has_prefix(seg, initial):
                ends_at[j].append((0, j, seg))
                len_at[j] = 1
            # Then try segments starting at position i
            for i in range(self.min_seg, j-self.min_seg+1):
                if ends_at[i] == []: continue
                seg = word[i:j]
                if self._has_prefix(seg, False):
                    ends_at[j].append((i, j, seg))
                    if len_at[j] is None or len_at[j] > 1+len_at[i]:
                        len_at[j] = 1+len_at[i]
                # Does this segment start with a doubled letter?
                if not (word[i-2] == word[i-1]): continue
                # If so, do the same thing as above, except 
                seg = word[i-1] + seg
                if self._has_prefix(seg, False):
                    ends_at[j].append((i, j, seg))
                    if len_at[j] is None or len_at[j] > 1+len_at[i]:
                        len_at[j] = 1+len_at[i]
        # Try to find candidates for the final segment
        j = len(word)
        for i in range(self.min_seg, len(word)-self.min_seg+1):
            if ends_at[i] == []: continue
            seg = word[i:]
            if self._has_suffix(seg, pos):
                ends_at[j].append((i, j, seg))
                if len_at[j] is None or len_at[j] > 1+len_at[i]:
                    len_at[j] = 1+len_at[i]
            # Does this segment start with a doubled letter?
            if not (word[i-2] == word[i-1]): continue
            seg = word[i-1] + seg
            # If so, do the same thing as above, except 
            if self._has_suffix(seg, pos):
                ends_at[j].append((i, j, seg))
                if len_at[j] is None or len_at[j] > 1+len_at[i]:
                    len_at[j] = 1+len_at[i]
        if ends_at[j] == []: return None
        if len_at[j] > 4: return None
        found = []
        Splitter.combine(found, ends_at, j, [], len_at[j])
        return found*/
	private List<String> analyze(String word, String pos,boolean initial)
	{
		StringBuffer analyzeBuf= new StringBuffer();
		HashMap<Integer,ArrayList<ServiceSupportModel>> endsAt=new HashMap<Integer,ArrayList<ServiceSupportModel>>();
		int []lenAt= new int[word.length()+1];
		for(int k=0;k<lenAt.length;k++)
		{
			lenAt[k]=-1;
		}
		 lenAt[0] = 0;

		for(int i= min_seg;i<word.length()-min_seg;i++)//TODO check +1 to min_seg
		{
			String seg= word.substring(0, i);//TODO ask  seg = word[:j]?
			if(hasPrefix(seg, initial)){
				ServiceSupportModel mod= new ServiceSupportModel();
				mod.setI(0);
				mod.setJ(i);
				mod.setSeg(seg);
				if(endsAt.containsKey(i))
				{
					ArrayList<ServiceSupportModel> temp=endsAt.get(i);
					temp.add(mod);
					endsAt.put(i,temp);	
				}
				else{
					ArrayList<ServiceSupportModel> temp=new ArrayList<ServiceSupportModel>();
					temp.add(mod);
					endsAt.put(i,temp);	
				}
				
				lenAt[i]=1;
			}
			for (int j=min_seg;j<i-min_seg;j++)
			{
				if(!endsAt.containsKey(j))
				{
					seg= word.substring(j, i);
					if(hasPrefix(seg, false))
					{
						ServiceSupportModel mod1= new ServiceSupportModel();
						mod1.setI(j);
						mod1.setJ(i);
						mod1.setSeg(seg);
						if(endsAt.containsKey(i))
						{
							ArrayList<ServiceSupportModel> temp=endsAt.get(i);
							temp.add(mod1);
							endsAt.put(i,temp);	
						}
						else{
							ArrayList<ServiceSupportModel> temp=new ArrayList<ServiceSupportModel>();
							temp.add(mod1);
							endsAt.put(i,temp);	
						}
						if(lenAt[i]==-1||lenAt[i]>1+lenAt[j])
						{
							lenAt[i]=1+lenAt[j];
						}
					}
					if (word.charAt(i)==word.charAt(j))
					{
						seg=word.charAt(j-1)+seg;
						if(hasPrefix(seg, false))
						{
							ServiceSupportModel mod1= new ServiceSupportModel();
							mod1.setI(j);
							mod1.setJ(i);
							mod1.setSeg(seg);
							if(endsAt.containsKey(i))
							{
								ArrayList<ServiceSupportModel> temp=endsAt.get(i);
								temp.add(mod1);
								endsAt.put(i,temp);	
							}
							else{
								ArrayList<ServiceSupportModel> temp=new ArrayList<ServiceSupportModel>();
								temp.add(mod1);
								endsAt.put(i,temp);	
							}
							if(lenAt[i]==-1||lenAt[i]>1+lenAt[j])
							{
								lenAt[i]=1+lenAt[j];
							}
						}
					}

				}
			}
			
			
		}
		int j= word.length();
		for (int i=min_seg;i<j-min_seg;i++)
		{
			if(!endsAt.containsKey(i))
			{
				String seg= word.substring(i, j);
				if(hasPrefix(seg, false))
				{
					ServiceSupportModel mod1= new ServiceSupportModel();
					mod1.setI(i);
					mod1.setJ(j);
					mod1.setSeg(seg);
					if(endsAt.containsKey(j))
					{
						ArrayList<ServiceSupportModel> temp=endsAt.get(i);
						temp.add(mod1);
						endsAt.put(j,temp);	
					}
					else{
						ArrayList<ServiceSupportModel> temp=new ArrayList<ServiceSupportModel>();
						temp.add(mod1);
						endsAt.put(j,temp);	
					}
					if(lenAt[j]==-1||lenAt[j]>1+lenAt[i])
					{
						lenAt[j]=1+lenAt[i];
					}
				}
				if (word.charAt(j)==word.charAt(i))
				{
					seg=word.charAt(i-1)+seg;
					if(hasPrefix(seg, false))
					{
						ServiceSupportModel mod1= new ServiceSupportModel();
						mod1.setI(i);
						mod1.setJ(j);
						mod1.setSeg(seg);
						if(endsAt.containsKey(j))
						{
							ArrayList<ServiceSupportModel> temp=endsAt.get(i);
							temp.add(mod1);
							endsAt.put(j,temp);	
						}
						else{
							ArrayList<ServiceSupportModel> temp=new ArrayList<ServiceSupportModel>();
							temp.add(mod1);
							endsAt.put(j,temp);	
						}
						if(lenAt[j]==-1||lenAt[j]>1+lenAt[i])
						{
							lenAt[j]=1+lenAt[i];
						}
					}
				}

			}
		}
		if(!endsAt.containsKey(j))
		{
			return null;
		}
		if(lenAt[j]>4)
		{
		return null;
		}
		List<String> strBuf= new ArrayList<String>();
		SaldoServiceImpl.combine(strBuf, j, endsAt, lenAt[j], "");
		return strBuf;
		
		
	}
	/*
	 * 
Splitter.combine(found, ends_at, j, [], len_at[j])
    @staticmethod
    def combine(found, ends_at, j, history, depth):
        if depth <= 0: return
        for i,_,seg in ends_at[j]:
            if i == 0:
                found.append([seg]+history)
            else:
                Splitter.combine(found, ends_at, i, [seg]+history, depth-1)

*/
	private static void combine(List<String> strBuf,int j, HashMap<Integer, ArrayList<ServiceSupportModel>> endsAt1, int lenAtJ, String history)
	{ 
		if(lenAtJ<=0)
		{
			return;
		}
		for(int i=0;i<endsAt1.get(j).size();i++) //TODO ask for i,_,seg in ends_at[j]
		{ 
			if(endsAt1.get(j).get(i).getJ()==0)
			{
				strBuf.add(endsAt1.get(j).get(i).getSeg()+history);
			}
			else
			{
				combine(strBuf,endsAt1.get(j).get(i).getJ(),endsAt1,lenAtJ-1,endsAt1.get(j).get(i).getSeg()+history);
			}
			
		}
		
	}
/*
 *   def get_ancestors(lemgram_ids):
            senses = set()
            for lemgram_id in lemgram_ids:
                if lemgram_id is None: continue
                senses |= set([
                    sense[0] for sense in
                    self.saldo.get_senses_by_lemgram(lemgram_id)])
            ancestors = set()
            for sense_id in senses:
                ancestors |= set(self.saldo.get_ancestors(sense_id, 2))
            return ancestors | senses

        word_ancestors = get_ancestors(
            [lemgram[0] for lemgram in self.saldo.get_lemgrams_by_form(word)])

        # TODO: make this more efficient by checking as we go
        seg_ancestors = get_ancestors(
            self.prefix_c.get(segs[0], set()) |
            self.prefix_ci.get(segs[0], set()))
        for middle in segs[1:-1]:
            seg_ancestors |= get_ancestors(
                self.prefix_c.get(middle, set()) |
                self.prefix_cm.get(middle, set()))
        seg_ancestors |= get_ancestors(
            self.suffix[pos].get(segs[-1], set()))

        #print self.prefix_c.get(segs[0], set())
        #print self.prefix_ci.get(segs[0], set())
        #print self.suffix[pos].get(segs[-1], set())
        #print 'ancestors of ', word.encode('utf-8')
        #print seg_ancestors, word_ancestors

        return len(seg_ancestors & word_ancestors) > 0
*/
	@Override
	public HashMap<String, List<Lemgram>> getAncestors(String lemgram) {
		List<Lemgram> senses = new ArrayList<Lemgram>();
		List<Lemgram> ancestors = new ArrayList<Lemgram>();
		List<Lemgram> lems = new ArrayList<Lemgram>();
		lems =lemDAO.getLemgramByName(lemgram);
		for(int i=0;i<lems.size();i++)
		{
		senses.addAll(lemDAO.getSense(lems.get(i).getSense()));
		}
		//TODO add ancestors parsing
		HashMap<String, List<Lemgram>> result =new HashMap<String, List<Lemgram>>();
		result.put("senses", senses);
		result.put("ancestors", ancestors);
		return result;

	}
	@Override
	public boolean isSemanticCompound(String word, String segs, String pos) {
		
		return hasSuffix(word, pos);
	}

}
