/*
 * 
 * 
 * 
 */
package accesslog;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 *
 * 
 */
public class Accesslog {

    private static Map <String, String> Month = new HashMap<>();
    private static Map <String, String> Day = new HashMap<>();
    private static Map <String, String> map = new HashMap<>();
 
    
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
    
        
        month(Month);      //月の英語表記から数値へ変換するための定義
        day(Day);          //日にちの一桁のものを二桁に変換するための定義

       //期間の指定//////////////////
        int start_date = 00000000;//期間に指定をしない場合二つの値を0にする
        int end_date   = 00000000;//(例：20191206)
       /////////////////////////////
      
       
       //期間の指定に間違いがあるとき終了
       if(start_date > end_date ){
           System.out.println("error:\n期間の指定に間違いがあります。");
           System.exit(0);
       }
       
       
       
        //入力するログファイル名を格納
        String filepass = "/var/log/httpd/access_log ";
        String filename1 = filepass + "\\\\\\\\\\.txt ";
        //String filename2 = filepass + "\\\\\\\\\\.txt ";
        //String filename3 = filepass + "\\\\\\\\\\.txt ";
        
        //追加するログファイルをリストにまとめる
        String[] filelist =  {filename1/*,filename2,filename3*/};
      
        //複数のファイルの連結
         FileLists mainlist = new FileLists(filelist);
        SequenceInputStream exSequence = new SequenceInputStream(mainlist);
    
        
         BufferedReader br = new BufferedReader(
               new InputStreamReader( exSequence ));
         
        
        
        try{
    
            int log_number = 0;   //アクセスログの総合計
            
            int flag_byte = 1;    //ファイルを最後まで読み終わったかの判断フラグ
                                  //0ならループ終了
            
           //ログ集計の変数
            int gyou = 4000000;                           //2GBに相当する行数 
            String[] all_access_log = new String[gyou];   //すべてのアクセスログを格納
            String[] time_log =  new String[gyou];        //日時に関するデータ
            int[] time_access_log =  new int[24];         //24時間ごとのデータ            
            String[] host_log =  new String[gyou];        //リモートホストの番号入れ
            
            for(int i = 0;i<100;i++)  time_log[i] = "";      //初期化
            for(int i = 0;i<24;i++)  time_access_log[i] = 0; //初期化
            for(int i = 0;i<100;i++)  host_log[i] = "";      //初期化
            
            
            
            
            
            
            ////////入力したファイルのデータの表示と格納/////////
            System.out.println("入力されたすべてのアクセスログ（%h %l %u %t \\\"%r\\\" %>s %b \\\"%{Referer}i\\\" \\\"%{User-Agent}i\\\"）");
           
            
            
            while(flag_byte == 1){//全アクセスログを読み込むまでループ
            
                int count_gyou = 0; //読み込んだ行数をカウントする変数
            
             while (true){//4百万行読み込む、もしくはファイルを読み終えたら抜ける
                                                     
               all_access_log[log_number] = br.readLine();
               
               if(all_access_log[log_number] != null){
               
               String[] data_1 = all_access_log[log_number].split(" ");
               
               
               System.out.println(String.format("%s", all_access_log[log_number]));
              
               host_log[log_number] = data_1[0];                   //リモートホストの番号取得
               time_log[log_number] = data_1[3].substring(1);      //時間の取得
                
               count_gyou += 1;
               log_number += 1;
               if(count_gyou == gyou)break;                        //gyou行を読み込んだらいったんループ抜ける
               }
               else if(all_access_log[log_number] == null){
                 flag_byte = 0;
                 break;
               }
               else System.out.println("error:\nファイルの読み込みで失敗しました");
              
             }
             
             
            ////////////////////////////////////////////////////////////
            System.out.println("\n");
            System.out.println("総合計：　"+log_number +"件のアクセス\n");

            
            
            if(start_date == 0 && end_date ==0) System.out.printf("指定された期間  無し\n");
            else System.out.printf("指定された期間：%9d～%9d\n",start_date,end_date);
            
            for(int i = 0;i<110;i++){
                System.out.print("-");
            }
            System.out.println("");
            
            ////////////取得した時間毎のアクセスデータの集計//////////////////////
            int i = 0;
            while (!"".equals(time_log[i])) {
       
                
              
                String[] nitizi = time_log[i].split("/");
                String[] zikan = nitizi[2].split(":");
                
                
                
                ////ここで日時ごとにデータの選別を行う////
                             
                int input_date = Integer.parseInt(zikan[0]+Month.get(nitizi[1])+Day.get(nitizi[0]));               //入力されたログの時間情報を使いやすいように変換
                
                if(input_date >= start_date && input_date <= end_date || start_date == 0 && end_date == 0){        //期間の判定
                
                switch (zikan[1]) {//具体的な1時間毎のアクセス回数の集計
                    
                    case "00":
                        time_access_log[0] += 1;
                        
                        break;
                    case "01":
                        time_access_log[1] += 1;
                        
                        break;
                    case "02":
                        time_access_log[2] += 1;
                        
                        break;
                    case "03":
                        time_access_log[3] += 1;
                        
                        break;
                    case "04":
                        time_access_log[4] += 1;
                        
                        break;
                    case "05":
                        time_access_log[5] += 1;
                        
                        break;
                    case "06":
                        time_access_log[6] += 1;
                        
                        break;
                    case "07":
                        time_access_log[7] += 1;
                        
                        break;
                    case "08":
                        time_access_log[8] += 1;
                        
                        break;
                    case "09":
                        time_access_log[9] += 1;
                        
                        break;
                    case "10":
                        time_access_log[10] += 1;
                        
                        break;
                    case "11":
                        time_access_log[11] += 1;
                        
                        break;
                    case "12":
                        time_access_log[12] += 1;
                        
                        break;
                    case "13":
                        time_access_log[13] += 1;
                        
                        break;
                    case "14":
                        time_access_log[14] += 1;
                        
                        break;
                    case "15":
                        time_access_log[15] += 1;
                        
                        break;
                    case "16":
                        time_access_log[16] += 1;
                        
                        break;
                    case "17":
                        time_access_log[17] += 1;
                        
                        break;
                    case "18":
                        time_access_log[18] += 1;
                        
                        break;
                    case "19":
                        time_access_log[19] += 1;
                        
                        break;
                    case "20":
                        time_access_log[20] += 1;
                        
                        break;
                    case "21":
                        time_access_log[21] += 1;
                        
                        break;
                    case "22":
                        time_access_log[22] += 1;
                        
                        break;
                    case "23":
                        time_access_log[23] += 1;
                        
                        break;    
                        
                    default:
                        
                        
                        break;
                }
                
           
             
            /////リモートホストの番号取得//////////
               int[] host_number = new int[100]; 
             
                for(int k = 0;k<100;k++)  host_number[k] = 0; //初期化
       
              
                   if(map.get(host_log[i]) == null){ //存在しないなら
                     
                     map.put(host_log[i], "1");
                    
                   }
                  else if(map.get(host_log[i]) != null){
                 
                    host_number[i] = Integer.parseInt(map.get(host_log[i]));
                    host_number[i] += 1;
                    map.put(host_log[i], String.valueOf(host_number[i]));
                    
                  }
               /////////////////////////////////////
      
            }
             i++;
           
           }
            ////////////////////////////////////////////////////////
            
           
            
            
            
            
            
            
             
             ////////時間毎のアクセスデータの表示/////////////////////
            System.out.println("時間別のアクセス件数（時間：件数）");
            
            for(int k=0;k<24;k++){
                System.out.printf("%3d時   ：  %3d件 |",k,time_access_log[k]);
                if(k%6 == 5) System.out.println("");
            }
            /////////////////////////////////////////////////////////
             System.out.println("\n");
           
            
            ////////リモートホスト別のアクセスデータの表示////////////////////
            System.out.println("リモートホスト別のアクセス件数（ホスト：件数）");
            
            //listの作成
            List<Entry<String, String>> list_entries = new ArrayList<>(map.entrySet());
            
            //連想配列のソート
            Collections.sort(list_entries, (Entry<String, String> obj1, Entry<String, String> obj2) -> obj2.getValue().compareTo(obj1.getValue()) //降順
            );
            
            //データの表示
            list_entries.stream().map((entry) -> {
                System.out.printf("%15s :%4s件",entry.getKey(),entry.getValue());
                return entry;
            }).forEachOrdered((_item) -> {
                System.out.println("");
            }); 
            ///////////////////////////////////////////////////////////////

            
         }
            br.close();
            
        }
        catch (IOException e) {
            System.out.println("error:\nファイルが見つかりません");
        
        }
       
        
    };
    
  
    
    
    
    private static void month(Map <String, String> month ) {
   
        //連想配列で英語での月表現を数字に変換
        month.put("Jan","01");
        month.put("Feb","02");
        month.put("Mar","03");
        month.put("Apr","04");
        month.put("May","05");
        month.put("Jun","06");
        month.put("July","07");
        month.put("Aug","08");
        month.put("Sept","09");
        month.put("Oct","10");
        month.put("Nov","11");
        month.put("Dec","12");
   
       }
    
    
    
    private static void day(Map <String, String> day) {
    
    //連想配列で一桁の数字も0を加えた二桁に変換
    day.put("1","01");
    day.put("2","02");
    day.put("3","03");
    day.put("4","04");
    day.put("5","05");
    day.put("6","06");
    day.put("7","07");
    day.put("8","08");
    day.put("9","09");
    day.put("10","10");
    day.put("11","11");
    day.put("12","12");
    day.put("13","13");
    day.put("14","14");
    day.put("15","15");
    day.put("16","16");
    day.put("17","17");
    day.put("18","18");
    day.put("19","19");
    day.put("20","20");
    day.put("21","21");
    day.put("22","22");
    day.put("23","23");
    day.put("24","24");
    day.put("25","25");
    day.put("26","26");
    day.put("27","27");
    day.put("28","28");
    day.put("29","29");
    day.put("30","30");
    day.put("31","31");
    }
    

    
    
    
    //複数のファイルをリストへSequenceInputStreamで扱えるように変換
    static class FileLists implements Enumeration {

    String[] FileLists;
    int current = 0;

    FileLists(String[] lileLists) {
        this.FileLists = lileLists;
    }

    @Override
    public boolean hasMoreElements() {
        return current < FileLists.length;
    }

    @Override
    public Object nextElement() {
        InputStream is = null;

        if (!hasMoreElements())
            throw new NoSuchElementException("No more files.");
        else {
            try {
                String nextElement = FileLists[current];
                current++;
                is = new FileInputStream(nextElement);
            } catch (FileNotFoundException e) {
                System.out.println("FileLists: " + e);
            }
        }
        return is;
    }
  }
}


