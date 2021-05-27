package com.buaa.texaspoker.util;

import com.buaa.texaspoker.entity.Poker;
import com.buaa.texaspoker.entity.PokerType;
import com.sun.jdi.Value;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PokerComparator implements Comparator<List<Poker>> {
    int[] list=new int[]{31,47,55,59,61,62,79,87,91,93,94,103,107,109,110,115,117,118,121,122,124};
    private boolean checkHJ(ArrayList al){
        int a0=(int)al.get(0);int a1=(int)al.get(1);int a2=(int)al.get(2);int a3=(int)al.get(3);int a4=(int)al.get(4);
        if(a0==2&&a1==3&&a2==4&&a3==5&&a4==14)return true;
        return false;
    }
    private int valueOfFivePokers(List<Poker> o){
        PokerType pokerType=o.get(0).getPokerType();
        int fl=0;
        ArrayList point=new ArrayList();
        point.add(o.get(0).getPoint());
        for(int i=1;i<5;i++){
            if(o.get(i).getPokerType()!=pokerType)fl=1;
            point.add(o.get(i).getPoint());
        }
        if(fl==0){
            point.sort(Comparator.naturalOrder());
            int ini= (int) point.get(0);
            int sig=0,sum=ini;
            for(int i=1;i<5;i++){
                if((int)point.get(i)-ini != 1){
                    sig=1;
                }
                sum+=(int)point.get(i);
                ini=(int)point.get(i);
            }
            if(sig==0){
                sum+=90000;
                return sum;
            }else{
                if(checkHJ(point)){
                    sum+=90000;
                    sum-=13;
                    return sum;
                }else{
                    if((int)point.get(0)==(int)point.get(3)){
                        int res=80000;
                        int i=(int)point.get(0);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(4);
                        return res;
                    }
                    if((int)point.get(1)==(int)point.get(4)){
                        int res=80000;
                        int i=(int)point.get(1);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(0);
                        return res;
                    }
                    if((int)point.get(0)==(int)point.get(2)&&(int)point.get(3)==(int)point.get(4)){
                        int res=70000;
                        int i=(int)point.get(0);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(4);
                        return res;
                    }
                    if((int)point.get(2)==(int)point.get(4)&&(int)point.get(0)==(int)point.get(1)){
                        int res=70000;
                        int i=(int)point.get(4);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(0);
                        return res;
                    }
                    int res=60000;
                    res+=sum;
                    return res;
                }
            }
        }else{
            point.sort(Comparator.naturalOrder());
            int ini= (int) point.get(0);
            int sig=0,sum=ini;
            for(int i=1;i<5;i++){
                if((int)point.get(i)-ini != -1){
                    sig=1;
                }
                sum+=(int)point.get(i);
                ini=(int)point.get(i);
            }
            if(sig==0){
                sum+=50000;
                return sum;
            }else{
                if(checkHJ(point)){
                    sum+=50000;
                    sum-=13;
                    return sum;
                }else{
                    if((int)point.get(0)==(int)point.get(3)){
                        int res=80000;
                        int i=(int)point.get(0);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(4);
                        return res;
                    }
                    if((int)point.get(1)==(int)point.get(4)){
                        int res=80000;
                        int i=(int)point.get(1);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(0);
                        return res;
                    }
                    if((int)point.get(0)==(int)point.get(2)&&(int)point.get(3)==(int)point.get(4)){
                        int res=70000;
                        int i=(int)point.get(0);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(4);
                        return res;
                    }
                    if((int)point.get(2)==(int)point.get(4)&&(int)point.get(0)==(int)point.get(1)){
                        int res=70000;
                        int i=(int)point.get(4);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(0);
                        return res;
                    }
                    if((int)point.get(0)==(int)point.get(2)){
                        int res=40000;
                        int i=(int)point.get(0);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(4);res+=(int)point.get(3);
                        return res;
                    }
                    if((int)point.get(2)==(int)point.get(4)){
                        int res=40000;
                        int i=(int)point.get(4);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(0); res+=(int)point.get(1);
                        return res;
                    }
                    if((int)point.get(0)==(int)point.get(1)){
                        if((int)point.get(2)==(int)point.get(3)){
                            int res=30000;
                            int i=(int)point.get(0)+(int)point.get(2);
                            i*=100;
                            res+=i;
                            res+=(int)point.get(4);
                            return res;
                        }else if((int)point.get(3)==(int)point.get(4)){
                            int res=30000;
                            int i=(int)point.get(0)+(int)point.get(3);
                            i*=100;
                            res+=i;
                            res+=(int)point.get(2);
                            return res;
                        }else{
                            int res=20000;
                            int i=(int)point.get(0);
                            i*=100;
                            res+=i;
                            res+=(int)point.get(2)+(int)point.get(3)+(int)point.get(4);
                            return res;
                        }
                    }
                    if((int)point.get(1)==(int)point.get(2)){
                        if((int)point.get(3)==(int)point.get(4)){
                            int res=30000;
                            int i=(int)point.get(1)+(int)point.get(3);
                            i*=100;
                            res+=i;
                            res+=(int)point.get(0);
                            return res;
                        }else{
                            int res=20000;
                            int i=(int)point.get(1);
                            i*=100;
                            res+=i;
                            res+=(int)point.get(0)+(int)point.get(3)+(int)point.get(4);
                            return res;
                        }
                    }
                    if((int)point.get(2)==(int)point.get(3)){
                        int res=20000;
                        int i=(int)point.get(2);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(1)+(int)point.get(0)+(int)point.get(4);
                        return res;
                    }
                    if((int)point.get(3)==(int)point.get(4)){
                        int res=20000;
                        int i=(int)point.get(3);
                        i*=100;
                        res+=i;
                        res+=(int)point.get(1)+(int)point.get(0)+(int)point.get(2);
                        return res;
                    }
                    return sum;
                }
            }
        }
    }
    private int valueOfList(List<Poker> o){
        List<Poker> tmp=new ArrayList<>();
        int ans=0;
        for(int x:list){
            tmp.clear();
            for(int j=0;j<=10;j++){
                int res=(x>>j)&1;
                if(res>0){
                    tmp.add(o.get(j));
                }
            }
            ans=Integer.max(ans,valueOfFivePokers(tmp));
        }
        return 1;
    }
    /**
     * 对比两种牌型的大小
     * @param o1 容量为 7 的牌型
     * @param o2 容量为 7 的牌型
     * @return 两种牌型的大小
     */
    @Override
    public int compare(List<Poker> o1, List<Poker> o2) {
        int v1=valueOfList(o1);
        int v2=valueOfList(o2);
        if(v1>v2)return 1;
        else if(v1<v2)return -1;
        return 0;
    }
}
