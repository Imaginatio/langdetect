package com.cybozu.labs.langdetect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import com.cybozu.labs.langdetect.util.LangProfile;

/**
 * Language Detector Factory Class
 * 
 * This class manages an initialization and constructions of {@link Detector}.
 * 
 * Before using language detection library,
 * load profiles with {@link DetectorFactory#loadProfile(String)} method
 * and set initialization parameters.
 * 
 * When the language detection,
 * construct Detector instance via {@link DetectorFactory#create()}.
 * See also {@link Detector}'s sample code.
 * 
 * <ul>
 * <li>4x faster improvement based on Elmer Garduno's code. Thanks!</li>
 * </ul>
 * 
 * @see Detector
 * @author Nakatani Shuyo
 */
public class DetectorFactory {
    public HashMap<String, double[]> wordLangProbMap;
    public ArrayList<String> langlist;
    private int langSize;
    private DetectorFactory() {
        wordLangProbMap = new HashMap<String, double[]>();
        langlist = new ArrayList<String>();
    }
    static private DetectorFactory instance_ = new DetectorFactory();

    /**
     * Load profiles from specified directory.
     * This method must be called once before language detection.
     * 
     * @param profileDirectory profile directory path
     * @throws LangDetectException  Can't open profiles(error code = {@link ErrorCode#FileLoadError})
     *                              or profile's format is wrong (error code = {@link ErrorCode#FormatError})
     * @throws IOException
     */
    public static void loadProfiles(String... langs) throws LangDetectException {
    	
    	for(int i=0; i<langs.length;i++){
    	
    		InputStream is = LangProfile.class.getClassLoader()
    				.getResourceAsStream("profiles/"+langs[i]);
    
            try {
                LangProfile profile = JSON.decode(is, LangProfile.class);
                addProfile(profile, langs.length);
            } catch (JSONException e) {
                throw new LangDetectException(ErrorCode.FormatError, "profile format error in for: "+langs[i]);
            } catch (IOException e) {
            	throw new LangDetectException(ErrorCode.FormatError, "profile format error in for: "+langs[i]);
			} finally {
                try {
                    if (is!=null) is.close();
                } catch (IOException e) {}
            }
        
    	}
    }

    /**
     * @param profile
     * @param langsize
     * @param index
     * @throws LangDetectException
     */
    static /* package scope */ void addProfile(LangProfile profile, int langsize) throws LangDetectException {
        String lang = profile.name;
        if (instance_.langlist.contains(lang)) {
            throw new LangDetectException(ErrorCode.DuplicateLangError, "duplicate the same language profile");
        }
        instance_.langlist.add(lang);
        for (String word: profile.freq.keySet()) {
            if (!instance_.wordLangProbMap.containsKey(word)) {
                instance_.wordLangProbMap.put(word, new double[langsize]);
            }
            double prob = profile.freq.get(word).doubleValue() / profile.n_words[word.length()-1];
            instance_.wordLangProbMap.get(word)[instance_.langlist.indexOf(profile.name)] = prob;
        }
    }

    /**
     * for only Unit Test
     */
    static /* package scope */ void clear() {
        instance_.langlist.clear();
        instance_.wordLangProbMap.clear();
    }

    /**
     * Construct Detector instance
     * 
     * @return Detector instance
     * @throws LangDetectException
     */
    static public Detector create() throws LangDetectException {
        return createDetector();
    }

    /**
     * Construct Detector instance with smoothing parameter
     * 
     * @param alpha smoothing parameter (default value = 0.5)
     * @param langSize number of profiles
     * @return Detector instance
     * @throws LangDetectException
     */
    public static Detector create(double alpha) throws LangDetectException {
        Detector detector = createDetector();
        detector.setAlpha(alpha);
        return detector;
    }

    static private Detector createDetector() throws LangDetectException {
        if (instance_.langlist.size()==0)
            throw new LangDetectException(ErrorCode.NeedLoadProfileError, "need to load profiles");
        Detector detector = new Detector(instance_);
        return detector;
    }
}
