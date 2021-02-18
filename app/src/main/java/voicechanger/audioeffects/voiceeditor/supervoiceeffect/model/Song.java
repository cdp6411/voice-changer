package voicechanger.audioeffects.voiceeditor.supervoiceeffect.model;

public class Song {
    public String Name;
    public String Path;

    public Song(String str, String str2) {
        this.Name = str;
        this.Path = str2;
    }

    public String getName() {
        return this.Name;
    }

    public String getPath() {
        return this.Path;
    }
}
