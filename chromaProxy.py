from flask import Flask, request, jsonify
import chromadb

app = Flask(__name__)
client = chromadb.HttpClient(host='localhost', port=8000)

#Them key de ko lo request luc SpringBoot Call qua
#Admin setting duplicate ratio
# Tuong đồng ngữ nghĩa
# Tương đồng về text
# Tương đồng cosin???
# Lúc duplicate, hiện bảng compare giữa  2 topic

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "ok"})


@app.route('/collections/<name>/add', methods=['POST'])
def add_to_collection(name):
    try:
        collection = client.get_or_create_collection(name)
        data = request.json

        #upsert - update neu trung id
        collection.upsert(
            ids=data['ids'],
            documents=data['documents'],
            metadatas=data.get('metadatas', None)
        )

        return jsonify({"status": "success", "message": f"Added {len(data['ids'])} documents"})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500


@app.route('/collections/<name>/delete', methods=['POST'])
def delete_from_collection(name):
    try:
        collection = client.get_collection(name)
        data = request.json

        collection.delete(ids=data['ids'])

        return jsonify({"status": "success"})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500


@app.route('/collections/<name>/query', methods=['POST'])
def query_collection(name):
    try:
        collection = client.get_collection(name)
        data = request.json

        results = collection.query(
            query_texts=data['query_texts'],
            n_results=data.get('n_results', 10)
        )

        return jsonify(results)
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

@app.route('/collections/<name>/count', methods=['GET'])
def count_collection(name):
    try:
        collection = client.get_collection(name)
        count = collection.count()
        return jsonify({"count": count})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500


# Get All
@app.route('/collections/<name>/get_all', methods=['GET'])
def get_all_from_collection(name):
    try:
        collection = client.get_collection(name)
        all_items = collection.get()
        return jsonify(all_items)
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5000)