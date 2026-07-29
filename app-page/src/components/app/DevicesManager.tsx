import { useState } from 'react';
import { toast } from 'diva-ui/components/sonner';
import { useT } from '@lib/i18n/useT';
import DataList from './DataList';
import type { Column } from './DataList';

interface DeviceData {
  id: string;
  name: string;
  created_at: number;
  updated_at: number;
}

interface DevicesManagerProps {
  uid: string;
  initialDevices: DeviceData[] | null;
  isVerified?: boolean;
  lang?: string;
}

function formatDate(ts?: number) {
  if (!ts) return '—';
  return new Date(ts).toLocaleString();
}

export default function DevicesManager({ uid, initialDevices, isVerified = true, lang = 'en' }: DevicesManagerProps) {
  const t = useT(lang);
  const [devices, setDevices] = useState<DeviceData[]>(initialDevices || []);
  const [refreshing, setRefreshing] = useState(false);

  const refetchDevices = async () => {
    setRefreshing(true);
    try {
      const res = await fetch(`/api/user/${uid}/devices`);
      if (res.ok) {
        const json = await res.json();
        setDevices(json.data || []);
      } else {
        toast.error(t('devicesPage.failedFetch'));
      }
    } catch {
      toast.error(t('devicesPage.failedFetch'));
    }
    setRefreshing(false);
  };

  const deviceColumns: Column<DeviceData>[] = [
    {
      key: 'name',
      header: t('devicesPage.name') || 'Name',
      render: (d: DeviceData) => (
        <span className="text-sm font-medium">{d.name || t('devicesPage.unknownDevice')}</span>
      ),
    },
    {
      key: 'created',
      header: t('devicesPage.created'),
      render: (d: DeviceData) => (
        <div className="text-muted-foreground text-xs whitespace-nowrap">
          {formatDate(d.created_at)}
        </div>
      ),
    },
    {
      key: 'lastSeen',
      header: t('devicesPage.lastSeen'),
      render: (d: DeviceData) => (
        <div className="text-muted-foreground text-xs whitespace-nowrap">
          {formatDate(d.updated_at)}
        </div>
      ),
    },
  ];

  return (
    <DataList
      columns={deviceColumns}
      data={devices}
      getId={(d: DeviceData) => d.id}
      selectable={false}
      emptyMessage={t('devicesPage.noDevices')}
      hasPermission={isVerified}
      toolbar={
        <div className="flex items-center justify-between px-6 py-4">
          <h3 className="font-semibold">{t('devicesPage.title')}</h3>
          <div className="flex items-center gap-2">
            <button
              className="inline-flex items-center justify-center rounded-md text-sm font-medium border border-input bg-background hover:bg-accent hover:text-accent-foreground h-9 px-4 py-2"
              onClick={refetchDevices}
              disabled={refreshing || !isVerified}
            >
              {refreshing ? t('devicesPage.refreshing') : t('devicesPage.refresh')}
            </button>
          </div>
        </div>
      }
    />
  );
}
