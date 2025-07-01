from db import get_recent_history

def generate_personalized_prompt(user_id, current_input, keywords):
    history = get_recent_history(user_id)
    
    relevant_context = ""
    for item in history:
        input_lower = item['input'].lower()
        if any(kw in input_lower for kw in keywords):
            if "see a doctor" in item['response'].lower():
                continue 
            relevant_context += f"User: {item['input']}\nAI: {item['response']}\n"

    prompt = (
        f"{relevant_context}"
        f"User just said: {current_input}\n"
        f"Give a helpful, calm, and medically responsible response.\n"
        f"Base your answer on the current symptoms. Only mention seeing a doctor if symptoms are clearly critical.\n"
        f"Keep it concise—within 150 words."
    )
    return prompt
