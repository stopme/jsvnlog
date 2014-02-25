package cn.jsvnlog.pojos;


import java.util.Map;
import java.util.Set;

public class CompareSVN {
    private Map<Integer, String> oldSvn;
    private Map<Integer, String> newSvn;
    private long add;
    private long delete;
    private long modify;
    public CompareSVN(Map<Integer, String> oldSvn,Map<Integer, String> newSvn){
        this.oldSvn  = oldSvn;
        this.newSvn = newSvn;
        init();
    }
    private void init(){
        Map<Integer, String> keyMap = this.oldSvn.size()<this.newSvn.size()?this.newSvn:this.oldSvn;
        Set<Integer> keys = keyMap.keySet();
        for (int key : keys) {
            String oldValue = this.oldSvn.get(key);
            String newValue =  this.newSvn.get(key);
            oldValue = oldValue==null?"no function":oldValue;
            newValue = newValue==null?"no function":newValue;
            //同一行如果一个为减，一个为加则表示修改1
            if(oldValue.startsWith("-") && newValue.startsWith("+")){
                modify =modify+1;
            }//同一行如果有减无加则为删除1
            else if(oldValue.startsWith("-") && !newValue.startsWith("+")){
                delete =delete+1;
            }//同一行如果无减有加则为加1
            else if(!oldValue.startsWith("-") && newValue.startsWith("+")){
                add =add+1;
            }
        }
        System.out.println("分别 增加"+add);
        System.out.println("分别删除"+delete);
        System.out.println("分别修改"+modify);
        System.out.println("==========================");
    }
    public long getDelete(){
        return delete;
        
    }
    public long getModify(){
        return modify;
        
    }
    public long getAdd(){
        return add;
        
    }
}