import weka.classifiers.Classifier;
import weka.core.Attribute;
import java.util.ArrayList;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.DenseInstance;
public class demo2 {
    private static Instance makeInstance(int signal1, int signal2, int signal3,int signal4,int signal5, int signal6, int signal7)
    {
        //instance配置和数据集内容相关，数据集包括classval，MYCT,MMIN,MMAX,CACH,CHMIN,CHMAX,CLASS8个属性，classval是目标分类结果，其他都是数据
        ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
        Attribute myct = new Attribute("MYCT");
        Attribute mmin = new Attribute("MMIN");
        Attribute mmax = new Attribute("MMAX");
        Attribute cach = new Attribute("CACH");
        Attribute chmin = new Attribute("CHMIN");
        Attribute chmax = new Attribute("CHMAX");
        Attribute class_0 = new Attribute("class");
        //设置目标分类结果。这里如果缺项会报错
        ArrayList<String> classval = new ArrayList<String>();
        classval.add("adviser");
        classval.add("amdahl");
        classval.add("apollo");
        classval.add("basf");
        classval.add("bti");
        classval.add("burroughs");
        classval.add("c.r.d");
        classval.add("cdc");
        classval.add("cambex");
        classval.add("dec");
        classval.add("dg");
        classval.add("formation");
        classval.add("four-phase");
        classval.add("gould");
        classval.add("hp");
        classval.add("harris");
        classval.add("honeywell");
        classval.add("ibm");
        classval.add("ipl");
        classval.add("magnuson");
        classval.add("microdata");
        classval.add("nas");
        classval.add("ncr");
        classval.add("nixdorf");
        classval.add("perkin-elmer");
        classval.add("prime");
        classval.add("siemens");
        classval.add("sperry");
        classval.add("sratus");
        classval.add("wang");
        Attribute vendor = new Attribute("vendor",classval);
        //这里向instances添加8个属性
        attributeList.add(vendor);
        attributeList.add(myct);
        attributeList.add(mmin);
        attributeList.add(mmax);
        attributeList.add(cach);
        attributeList.add(chmin);
        attributeList.add(chmax);
        attributeList.add(class_0);
        //需要新建一个instances，然后将生理信号包装成一个instance(inst)，再把这个instance添加进instances(data)
        Instances data = new Instances("testInstances", attributeList,1);
        Instance inst = new DenseInstance(8);
        data.add(inst);
        inst.setValue(myct, signal1);
        inst.setValue(mmin , signal2);
        inst.setValue(mmax , signal3);
        inst.setValue(cach , signal4);
        inst.setValue(chmin, signal5);
        inst.setValue(chmax, signal6);
        inst.setValue(class_0, signal7);
        inst.setDataset(data);
        data.setClassIndex(0);

        return inst;
    }
    private static double inference_function_fusion(Classifier classifier, int signal1, int signal2, int signal3, int signal4, int signal5, int signal6, int signal7) throws Exception {
        //首先将输入的7个信号包装成instance形状
        Instance inst = makeInstance(signal1, signal2, signal3, signal4, signal5, signal6, signal7);
        //将包装好的instance丢进分类器
        double result = classifier.classifyInstance(inst);
        return result;
    }

    public static void main(String[] args) throws Exception{
        //载入分类器
        Classifier classifier = (Classifier) weka.core.SerializationHelper.read("LibSVM.model");
        //设置输入信号
        int signal1 = 167;
        int signal2 = 524;
        int signal3 = 2000;
        int signal4 = 8;
        int signal5 = 4;
        int signal6 = 15;
        int signal7 = 23;
        //将各种信号丢入分类器进行分类
        double result = inference_function_fusion(classifier,signal1, signal2, signal3, signal4, signal5, signal6, signal7);
        System.out.println(result);

    }

}
