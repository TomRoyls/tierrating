export interface TierlistEntry {
	id: string;
	score: number;
	title: string;
	cover: string;
	tier: Tier;
	state: string;
	index: number;
}

export interface Tier {
	name: string;
	score: number;
	adjustedScore: number;
	color: string;
}
