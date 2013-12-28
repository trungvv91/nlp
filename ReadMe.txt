Chương trình NLP-Tools
1. Thư mục chương trình
	- /Command: Chương trình dòng lệnh của chương trình VietChunker.
	- /corpus: Chứa tập văn bản thực nghiệm
		- corpus/AutoSummary: Thư mục văn bản đầu ra chương trình NLP-Tools
		- corpus/Plaintext: Thư mục văn bản đầu vào chương trình NLP-Tools
		- corpus/Summary: Thư mục văn bản tóm tắt bằng tay
		***Filename của 1 văn bản tương ứng: Văn bản gốc, Bằng máy, Bằng tay là giống nhau
	- /data: File sinh ra từ pha Tokenize, Postag, Chunker và các file để chạy VietChunker
	- /lib: Các thư viện sử dụng
	- /train-data: Folder file từ StopWords, Synomym...
		- idf_final.txt: idf của một từ trong corpus đầy đủ
		- idf_index_test.txt: idf của corpus nhỏ
	- /src: file mã nguồn

2. Source Code:
	- nlp.dict Package:
		- Conjunction.java: Các conjunction dùng cho bước cắt tỉa theo cấu trúc diễn ngôn
		- NounAnophoric: Luật Phân giải đồng tham chiếu
		- StopWords.java: Mảng Stop-Words
		- Synonym.java: Đọc danh sách từ đồng nghĩa
		- Idf_index.java: Tính idf của các từ trong corpus (Corpus tính idf là corpus văn bản của báo mới - khoảng hơn 20000 văn bản đã tách từ)
		- Idf_normalize.java: Correct tách từ sai từ corpus
		***Hai file Idf chạy riêng và độc lập với chương trình Summary
	- nlp.display Package: Chương trình giao diện
	- nlp.graph:
		- Edge.java: Edge entity
		- Vertex.java: Vertex entity
		- Main.java: File chạy chương trình. Input: /corpus/Plaintext. Output: /corpus/AutoSummary
		- WordsGraph.java: Chương trình chính chạy các luật
		- Evaluation.java: tính độ ROUGE 
	- nlp.sentenceExtraction: trích rút câu
		- Datum.java: mỗi từ sẽ coi là một Datum với các thuộc tính của nó
		- SentenceExtration.java: File thực hiện trích rút câu
		- SentenceRedundancy.java: Tính độ đo tương đồng giữa 2 câu và loại bỏ câu dư thừa
		- idf_score.java: đọc độ đo idf từ file idf_index_test.txt hoặc idf_final.txt
		
	- nlp.tool.vnTextPro
		- VNPreprocessing.java: tiền xử lý văn bản: loại bỏ các từ trong dấu ( ) ...
		- VNSentenceSegmenter.java: tách câu
		- VNTokenizer.java: tách từ
		- VNTagger.java: postager
		