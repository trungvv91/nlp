package nlp.tool.vnTextPro;

import java.util.ArrayList;
import java.util.List;

import jvnsensegmenter.JVnSenSegmenter;


public class VNSentenceSegmenter {
	private JVnSenSegmenter segmenter;

	public VNSentenceSegmenter(){ this("models/jvntextpro/jvnsensegmenter"); }

	public VNSentenceSegmenter(String modelDir){ init(modelDir); }

	private void init(String modelDir){
		segmenter = new JVnSenSegmenter();
		segmenter.init(modelDir);
	}

	public String[] segment(String text){
		List<String> holder = new ArrayList<String>();
		segmenter.senSegment(text, holder);
		return holder.toArray(new String[holder.size()]);
	}
	
	public void segmentFile(String inputFile, String outputFile ){
		JVnSenSegmenter.senSegmentFile(inputFile, outputFile, segmenter);
	}

	public static void main(String[] args){
		VNSentenceSegmenter senSegmenter = new VNSentenceSegmenter();
		String text = "Theo tin thì chỉ mới bán các phần phụ của cây sưa như đe, rễ, cành và ngọn; " +
				"mỗi kg có giá từ 2 - 15.5 triệu đồng. Riêng phần chính là 110 phác gỗ mặt của 3 cây sưa đã được lâm tặc chôn giấu an toàn, " +
				"chờ đến thời điểm thích hợp mới đưa ra ... Nói chung là thế";
		int count = 1;
		for(String sen: senSegmenter.segment(text))
			System.out.println((count++) + ". " + sen);
	}
}
