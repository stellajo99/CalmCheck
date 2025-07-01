import requests
import os
from dotenv import load_dotenv

load_dotenv()

LLAMA_API_URL = "https://openrouter.ai/api/v1/chat/completions"
LLAMA_MODEL = "meta-llama/llama-4-maverick:free" 

def query_llama(prompt):
    headers = {
        "Authorization": f"Bearer {os.getenv('OPENROUTER_API_KEY')}",
        "Content-Type": "application/json"
    }
    body = {
        "model": LLAMA_MODEL,
        "messages": [{"role": "user", "content": prompt}]
    }

    try:
        res = requests.post(LLAMA_API_URL, headers=headers, json=body)
        if res.status_code == 200:
            return res.json()["choices"][0]["message"]["content"]
        else:
            print(f"LLAMA Error {res.status_code}: {res.text}")
            return "(AI error) Unable to process your request at the moment."
    except Exception as e:
        print("Exception in llama_client:", e)
        return "(AI error) Exception occurred while contacting LLaMA."
