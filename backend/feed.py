import os
import requests
from dotenv import load_dotenv

load_dotenv()
GNEWS_API_KEY = os.getenv("GNEWS_API_KEY")

def get_feed():
    keywords = ["health", "nutrition", "wellbeing"]
    results = []
    seen_urls = set()

    for keyword in keywords:
        query = keyword.replace(" ", "+") 
        url = f"https://gnews.io/api/v4/search?q={query}&lang=en&max=5&token={GNEWS_API_KEY}"

        try:
            res = requests.get(url)
            res.raise_for_status()
            articles = res.json().get("articles", [])

            for a in articles:
                url = a.get("url", "")
                if url and url not in seen_urls:
                    seen_urls.add(url)
                    results.append({
                        "title": a.get("title", ""),
                        "summary": a.get("description", ""),
                        "imageUrl": a.get("image", ""),
                        "articleUrl": url
                    })

        except Exception as e:
            print(f"❌ Error fetching for keyword '{keyword}':", e)

    return results[:5] if results else [{
        "title": "No health articles found",
        "summary": "",
        "imageUrl": "",
        "articleUrl": ""
    }]
