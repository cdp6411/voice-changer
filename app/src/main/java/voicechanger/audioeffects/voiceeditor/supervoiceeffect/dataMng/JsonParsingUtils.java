package voicechanger.audioeffects.voiceeditor.supervoiceeffect.dataMng;

import voicechanger.audioeffects.voiceeditor.supervoiceeffect.constants.IVoiceChangerConstants;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.object.EffectObject;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.DBLog;
import voicechanger.audioeffects.voiceeditor.supervoiceeffect.utils.StringUtils;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParsingUtils implements IVoiceChangerConstants {
    public static final String TAG = "JsonParsingUtils";

    public static ArrayList<EffectObject> parsingListEffectObject(String str) {
        if (!StringUtils.isEmptyString(str)) {
            try {
                JSONArray jSONArray = new JSONArray(str);
                int length = jSONArray.length();
                if (length > 0) {
                    ArrayList<EffectObject> arrayList = new ArrayList();
                    for (int i = 0; i < length; i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        String string = jSONObject.getString("id");
                        String string2 = jSONObject.getString("name");
                        int i2 = jSONObject.getInt("pitch");
                        int i3 = jSONObject.getInt("rate");
                        boolean z = jSONObject.getBoolean("flanger");
                        boolean z2 = jSONObject.opt("reverse") != null ? jSONObject.getBoolean("reverse") : false;
                        boolean z3 = jSONObject.opt("echo") != null ? jSONObject.getBoolean("echo") : false;
                        EffectObject effectObject = new EffectObject(string, string2, i2, (float) i3);
                        effectObject.setFlanger(z);
                        effectObject.setReverse(z2);
                        effectObject.setEcho(z3);
                        arrayList.add(effectObject);
                        JSONArray jSONArray2 = jSONObject.getJSONArray("reverb");
                        if (jSONArray2.length() > 0) {
                            float[] fArr = new float[3];
                            for (i3 = 0; i3 < 3; i3++) {
                                fArr[i3] = (float) jSONArray2.getDouble(i3);
                            }
                            effectObject.setReverb(fArr);
                        }
                        if (jSONObject.opt("eq") != null) {
                            JSONArray jSONArray3 = jSONObject.getJSONArray("eq");
                            if (jSONArray3.length() > 0) {
                                float[] fArr2 = new float[3];
                                for (int i4 = 0; i4 < 3; i4++) {
                                    fArr2[i4] = (float) jSONArray3.getDouble(i4);
                                }
                                effectObject.setEq(fArr2);
                            }
                        }
                    }
                    str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("===================>size effect =");
                    stringBuilder.append(arrayList.size());
                    DBLog.m11d(str, stringBuilder.toString());
                    return arrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
