import spacy

nlp = spacy.load("en_core_web_sm")

def extract_keywords(text):
    doc = nlp(text)
    return list(set([token.lemma_ for token in doc if token.pos_ in ['NOUN', 'ADJ'] and not token.is_stop]))

def summarize_response(text):
    return text[:100] + "..." if len(text) > 100 else text

def determine_severity(user_input, ai_response):
    text = (user_input + " " + ai_response).lower()

    # Only match critical words from *user input*, not AI speculation
    if any(word in user_input.lower() for word in ["emergency", "bleeding", "unconscious", "severe", "chest pain"]):
        return "high"
    elif any(word in text for word in ["mild", "slight", "temporary", "headache"]):
        return "low"
    else:
        return "moderate"


def contains_critical_symptom(user_input):
    critical_keywords = {"chest pain", "suicidal", "shortness of breath", "bleeding", "fainting", "seizure"}
    text = user_input.lower()
    return any(keyword in text for keyword in critical_keywords)