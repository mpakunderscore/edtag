function add_url(tab_url, tags, title, favIconUrl, usertags) {
	
	var heroku = "quiet-anchorage-6418.herokuapp.com";
	var localhost = "localhost:9000";
	
	var url = "http://" + heroku + "/add?url=" + tab_url + "&tags=" + JSON.stringify(tags) + "&title=" + title + "&faviconurl=" + favIconUrl + "&usertags=" + JSON.stringify(usertags);

	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("POST", url, false);
	xmlHttp.send(null);
	
	var domen = tab_url.replace("http://", "").split("/")[0];
	
	//chrome (autohide)
	// chrome.notifications.create(
	// 	
	//     tab_url, {   
	// 		type: 'basic', 
	// 		iconUrl: favIconUrl, 
	// 		title: title, 
	// 		message: '[' + domen + ']' + '[' + Object.keys(usertags).join(", ") + ']' +  Object.keys(tags).join(", ") 
	//     }, function() {} );
					
	//webkit
	var notification = window.webkitNotifications.createNotification(
		favIconUrl,                      // The image.
		title, // The title.
		'[' + domen + '] [' + Object.keys(usertags).join(", ") + '] ' +  Object.keys(tags).join(", ")      // The body.
	);
			
	notification.show();		
}



function words_process(words_map) {
	
	return sort_remove(sugar(merger(words_map)));
}

//just sort 2 > 1 and return array
function sort_remove(words_map) {

	var sortable = [];
	for (var word in words_map) 
		if (words_map[word] != null)
			sortable.push([word, words_map[word]]);
	
	sortable.sort(function(b, a) {return a[1] - b[1]})		  
		
	return sortable;
}

//simple check for plural
function merger(words_map) {
	
	for (var word in words_map) {
				
		var singular = word.length - 1;
		
		if (word.substr(singular, word.length) === 's' 
			&& words_map[word.substr(0, singular)] != null) {
				
				words_map[word.substr(0, singular)] += words_map[word];
				words_map[word] = null;
		}
	}
	
	return words_map;
}

//simple check for exceptions
function sugar(words_map) {

	var exceptions = getWordExceptions();
	
	for (var word in words_map) {
		
		if (exceptions.indexOf(word) > -1) 
			words_map[word] = null;
	}
	
	return words_map;
}


//check words and move this out
function getWordExceptions() {
	
	var out = [];
	
	var thousand = ['a', 'able', 'about', 'above', 'according', 'account', 'across', 'act', 'action', 'activities', 'activity', 'actually', 'added', 'addition', 'additional', 'administration', 'after', 'again', 'against', 'age', 'ago', 'ahead', 'aid', 'air', 'all', 'almost', 'alone', 'along', 'already', 'also', 'although', 'always', 'am', 'america', 'american', 'among', 'amount', 'an', 'analysis', 'and', 'another', 'answer', 'anti', 'any', 'anyone', 'anything', 'apparently', 'appear', 'appeared', 'approach', 'are', 'area', 'areas', 'arms', 'army', 'around', 'art', 'as', 'ask', 'asked', 'association', 'at', 'attack', 'attention', 'audience', 'available', 'average', 'away', 'b', 'back', 'bad', 'ball', 'based', 'basic', 'basis', 'be', 'beautiful', 'became', 'because', 'become', 'bed', 'been', 'before', 'began', 'beginning', 'behind', 'being', 'believe', 'below', 'best', 'better', 'between', 'beyond', 'big', 'bill', 'black', 'blood', 'blue', 'board', 'body', 'book', 'born', 'both', 'boy', 'boys', 'bring', 'british', 'brought', 'brown', 'building', 'built', 'business', 'but', 'by', 'c', 'call', 'called', 'came', 'can', 'cannot', 'cant', 'car', 'care', 'carried', 'cars', 'case', 'cases', 'cause', 'cent', 'center', 'central', 'century', 'certain', 'certainly', 'chance', 'change', 'changes', 'character', 'charge', 'chief', 'child', 'children', 'choice', 'christian', 'church', 'city', 'class', 'clear', 'clearly', 'close', 'closed', 'club', 'co', 'cold', 'college', 'color', 'come', 'comes', 'coming', 'committee', 'common', 'communist', 'community', 'company', 'complete', 'completely', 'concerned', 'conditions', 'congress', 'consider', 'considered', 'continued', 'control', 'corner', 'corps', 'cost', 'costs', 'could', 'couldnt', 'countries', 'country', 'county', 'couple', 'course', 'court', 'covered', 'cut', 'd', 'daily', 'dark', 'data', 'day', 'days', 'de', 'dead', 'deal', 'death', 'decided', 'decision', 'deep', 'defense', 'degree', 'democratic', 'department', 'described', 'design', 'designed', 'determined', 'developed', 'development', 'did', 'didnt', 'difference', 'different', 'difficult', 'direct', 'direction', 'directly', 'distance', 'district', 'do', 'does', 'doing', 'done', 'dont', 'door', 'doubt', 'down', 'dr', 'drive', 'due', 'during', 'e', 'each', 'earlier', 'early', 'earth', 'east', 'easy', 'economic', 'education', 'effect', 'effective', 'effects', 'effort', 'efforts', 'eight', 'either', 'elements', 'else', 'end', 'england', 'english', 'enough', 'entire', 'equipment', 'especially', 'established', 'europe', 'even', 'evening', 'ever', 'every', 'everything', 'evidence', 'example', 'except', 'existence', 'expect', 'expected', 'experience', 'extent', 'eye', 'eyes', 'f', 'face', 'fact', 'faith', 'fall', 'family', 'far', 'farm', 'father', 'fear', 'federal', 'feed', 'feel', 'feeling', 'feet', 'felt', 'few', 'field', 'figure', 'figures', 'filled', 'final', 'finally', 'find', 'fine', 'fire', 'firm', 'first', 'fiscal', 'five', 'floor', 'followed', 'following', 'food', 'foot', 'for', 'force', 'forces', 'foreign', 'form', 'former', 'forms', 'forward', 'found', 'four', 'free', 'freedom', 'french', 'friend', 'friends', 'from', 'front', 'full', 'function', 'further', 'future', 'g', 'game', 'gave', 'general', 'generally', 'george', 'get', 'getting', 'girl', 'girls', 'give', 'given', 'gives', 'glass', 'go', 'god', 'going', 'gone', 'good', 'got', 'government', 'great', 'greater', 'green', 'ground', 'group', 'groups', 'growing', 'growth', 'gun', 'h', 'had', 'hair', 'half', 'hall', 'hand', 'hands', 'happened', 'hard', 'has', 'have', 'having', 'he', 'head', 'hear', 'heard', 'heart', 'heavy', 'held', 'hell', 'help', 'her', 'here', 'herself', 'hes', 'high', 'higher', 'him', 'himself', 'his', 'history', 'hit', 'hold', 'home', 'hope', 'horse', 'hospital', 'hot', 'hotel', 'hour', 'hours', 'house', 'how', 'however', 'human', 'hundred', 'husband', 'i', 'idea', 'ideas', 'if', 'ill', 'im', 'image', 'immediately', 'important', 'in', 'include', 'including', 'income', 'increase', 'increased', 'indeed', 'individual', 'industrial', 'industry', 'influence', 'information', 'inside', 'instead', 'interest', 'international', 'into', 'involved', 'is', 'island', 'issue', 'it', 'its', 'itself', 'ive', 'j', 'job', 'john', 'just', 'justice', 'keep', 'kennedy', 'kept', 'kind', 'knew', 'know', 'knowledge', 'known', 'l', 'labor', 'lack', 'land', 'language', 'large', 'larger', 'last', 'late', 'later', 'latter', 'law', 'lay', 'lead', 'leaders', 'learned', 'least', 'leave', 'led', 'left', 'length', 'less', 'let', 'letter', 'letters', 'level', 'life', 'light', 'like', 'likely', 'line', 'lines', 'list', 'literature', 'little', 'live', 'lived', 'living', 'local', 'long', 'longer', 'look', 'looked', 'looking', 'lost', 'lot', 'love', 'low', 'lower', 'm', 'made', 'main', 'major', 'make', 'makes', 'making', 'man', 'manner', 'mans', 'many', 'march', 'market', 'married', 'mass', 'material', 'matter', 'may', 'maybe', 'me', 'mean', 'meaning', 'means', 'medical', 'meet', 'meeting', 'member', 'members', 'men', 'merely', 'met', 'method', 'methods', 'middle', 'might', 'miles', 'military', 'million', 'mind', 'minutes', 'miss', 'modern', 'moment', 'money', 'month', 'months', 'moral', 'more', 'morning', 'most', 'mother', 'move', 'moved', 'movement', 'moving', 'mr', 'mrs', 'much', 'music', 'must', 'my', 'myself', 'n', 'name', 'nation', 'national', 'nations', 'natural', 'nature', 'near', 'nearly', 'necessary', 'need', 'needed', 'needs', 'negro', 'neither', 'never', 'new', 'next', 'night', 'no', 'non', 'nor', 'normal', 'north', 'not', 'note', 'nothing', 'now', 'nuclear', 'number', 'numbers', 'obtained', 'obviously', 'of', 'off', 'office', 'often', 'oh', 'old', 'on', 'once', 'one', 'ones', 'only', 'open', 'opened', 'operation', 'opportunity', 'or', 'order', 'organization', 'other', 'others', 'our', 'out', 'outside', 'over', 'own', 'p', 'paid', 'paper', 'part', 'particular', 'particularly', 'parts', 'party', 'passed', 'past', 'pattern', 'pay', 'peace', 'people', 'per', 'performance', 'perhaps', 'period', 'person', 'personal', 'persons', 'physical', 'picture', 'piece', 'place', 'placed', 'plan', 'plane', 'planning', 'plans', 'plant', 'play', 'point', 'points', 'police', 'policy', 'political', 'pool', 'poor', 'population', 'position', 'possible', 'post', 'power', 'present', 'president', 'press', 'pressure', 'price', 'principle', 'private', 'probably', 'problem', 'problems', 'process', 'production', 'products', 'program', 'programs', 'progress', 'property', 'provide', 'provided', 'public', 'purpose', 'put', 'quality', 'question', 'questions', 'quite', 'r', 'race', 'radio', 'ran', 'range', 'rate', 'rather', 'reached', 'reaction', 'read', 'reading', 'ready', 'real', 'really', 'reason', 'received', 'recent', 'recently', 'record', 'red', 'religion', 'religious', 'remember', 'report', 'reported', 'required', 'research', 'respect', 'responsibility', 'rest', 'result', 'results', 'return', 'returned', 'right', 'river', 'road', 'room', 'run', 'running', 's', 'said', 'sales', 'same', 'sat', 'saw', 'say', 'saying', 'says', 'school', 'schools', 'science', 'season', 'second', 'secretary', 'section', 'see', 'seem', 'seemed', 'seems', 'seen', 'self', 'sense', 'sent', 'series', 'serious', 'served', 'service', 'services', 'set', 'seven', 'several', 'shall', 'she', 'short', 'shot', 'should', 'show', 'showed', 'shown', 'side', 'similar', 'simple', 'simply', 'since', 'single', 'situation', 'six', 'size', 'slowly', 'small', 'so', 'social', 'society', 'some', 'something', 'sometimes', 'somewhat', 'son', 'soon', 'sort', 'sound', 'south', 'southern', 'soviet', 'space', 'speak', 'special', 'specific', 'spirit', 'spring', 'square', 'st', 'staff', 'stage', 'stand', 'standard', 'start', 'started', 'state', 'statements', 'states', 'stay', 'step', 'steps', 'still', 'stock', 'stood', 'stop', 'stopped', 'story', 'straight', 'street', 'strength', 'strong', 'student', 'students', 'study', 'subject', 'such', 'suddenly', 'summer', 'sun', 'support', 'sure', 'surface', 'system', 'systems', 't', 'table', 'take', 'taken', 'taking', 'talk', 'tax', 'technical', 'tell', 'temperature', 'ten', 'term', 'terms', 'test', 'th', 'than', 'that', 'thats', 'the', 'their', 'them', 'themselves', 'then', 'theory', 'there', 'therefore', 'theres', 'these', 'they', 'thing', 'things', 'think', 'thinking', 'third', 'thirty', 'this', 'those', 'thought', 'three', 'through', 'through', 'throughout', 'thus', 'time', 'times', 'to', 'today', 'together', 'told', 'too', 'took', 'top', 'total', 'toward', 'town', 'trade', 'training', 'treatment', 'trial', 'tried', 'trouble', 'true', 'truth', 'try', 'trying', 'turn', 'turned', 'twenty', 'two', 'type', 'types', 'u', 'under', 'understand', 'understanding', 'union', 'united', 'university', 'until', 'up', 'upon', 'us', 'use', 'used', 'using', 'usually', 'value', 'values', 'various', 'very', 'view', 'voice', 'volume', 'waiting', 'walked', 'wall', 'want', 'wanted', 'war', 'was', 'washington', 'wasnt', 'water', 'way', 'ways', 'we', 'week', 'weeks', 'well', 'went', 'were', 'west', 'western', 'what', 'whatever', 'when', 'where', 'whether', 'which', 'while', 'white', 'who', 'whole', 'whom', 'whose', 'why', 'wide', 'wife', 'will', 'william', 'window', 'wish', 'with', 'within', 'without', 'woman', 'women', 'word', 'words', 'work', 'worked', 'working', 'works', 'world', 'would', 'wouldnt', 'writing', 'written', 'wrong', 'wrote', 'year', 'years', 'yes', 'yet', 'york', 'you', 'young', 'your', 'youre'];	
	out += thousand;
	
	//time
	out += ['feb'];
	
	//mine
	out += ['created', 'shows', 'someone', 'join', 'jump', 'etc', 'wikipedia', 'wikimedia'];
	
	//reddit
	out += ['commentsharesavehidereport', 'commentssharesavehidereport', 'submitted', 'replies', 'permalinkparentreportreply', 'reddit', 'goldreply', 'comments', 'load', 'permalinkparentreportgive', 'rgood', 'permalinkreportgive', 'edit', 'thread', 'inbox', 'klib', 'deleted'];		
	
	 //www
	out += ['http', 'link', 'page', 'wikipedia', 'edit', 'retrieved', 'www'];
	
	return out;
}


